package spookyspirits.entity;

import java.util.EnumSet;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PossessedPumpkin extends MonsterEntity {
	
	private static final DataParameter<Byte> STANDING_TICKS = EntityDataManager.createKey(PossessedPumpkin.class, DataSerializers.BYTE);
	private static final DataParameter<Boolean> IS_STANDING_UP = EntityDataManager.createKey(PossessedPumpkin.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_TARGET_LOOKING = EntityDataManager.createKey(PossessedPumpkin.class, DataSerializers.BOOLEAN);

	private static final String KEY_STANDING_TICKS = "StandingTicks";
	private static final String KEY_IS_STANDING_UP = "IsStandingUp";
	private static final String KEY_IS_TARGET_LOOKING = "IsTargetLooking";
	
	/** Must be less than or equal to Byte.MAX_VALUE [127] **/
	public static final int MAX_STANDING_TICKS = 10;

	public PossessedPumpkin(EntityType<? extends PossessedPumpkin> type, World world) {
		super(type, world);
		this.stepHeight = 1.0F;
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(3, new StandingSittingGoal(this));
		this.goalSelector.addGoal(4, new MoveTowardTargetGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new LeapAtTargetGoal(this, 0.4F));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.31F);
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(STANDING_TICKS, Byte.valueOf((byte)0));
		this.getDataManager().register(IS_STANDING_UP, Boolean.valueOf(false));
		this.getDataManager().register(IS_TARGET_LOOKING, Boolean.valueOf(false));
	}

	@Override
	public void livingTick() {
		super.livingTick();
//		if(this.ticksExisted % 20 == 0 && closestPlayer != null) {
//			System.out.println("Can be seen: " + canBeSeen(closestPlayer));
//		}
	}
	
	@Override
	public void travel(final Vec3d vec) {
		super.travel(vec);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putByte(KEY_STANDING_TICKS, (byte)this.getStandingTicks());
		compound.putBoolean(KEY_IS_STANDING_UP, this.isStandingUp());
		compound.putBoolean(KEY_IS_TARGET_LOOKING, this.isTargetLooking());

	}

	@Override
	public void readAdditional(final CompoundNBT compound) {
		super.readAdditional(compound);
		this.setStandingTicks(compound.getByte(KEY_STANDING_TICKS));
		this.setStandingState(compound.getBoolean(KEY_IS_STANDING_UP));
		this.setTargetLooking(compound.getBoolean(KEY_IS_TARGET_LOOKING));
	}
	
	/**
	 * Snaps the entity position and rotation to the nearest block
	 **/
	public void hide() {
		// snap position
		final double x = Math.round(posX - 0.5D) + 0.5D;
		final double z = Math.round(posZ - 0.5D) + 0.5D;
		this.setPosition(x, this.posY, z);
		// snap direction
		final float yaw = ((int)(this.rotationYaw / 90F)) * 90F;
		this.rotateTowards(yaw, this.rotationPitch);
	}
	
	/**
	 * Adapted from Enderman code. Detects if the player is facing
	 * towards the entity, with a bit of fuzz factor 
	 * @param entity the player (usually)
	 * @return true if the given entity is facing towards this entity
	 **/
	public boolean canBeSeen(final LivingEntity entity) {
		// higher fuzz = wider range of vectors
		final double fuzz = 2.0D;
		Vec3d vec3d = entity.getLook(1.0F).normalize();
		Vec3d vec3d1 = new Vec3d(this.posX - entity.posX, this.getBoundingBox().minY + (double) this.getEyeHeight()
				- (entity.posY + (double) entity.getEyeHeight()), this.posZ - entity.posZ);
		double d0 = vec3d1.length();
		vec3d1 = vec3d1.normalize();
		double d1 = vec3d.dotProduct(vec3d1);
		return d1 > fuzz / d0 && entity.canEntityBeSeen(this);
	}

	/**
	 * @return true if the entity's legs are completely ready to walk
	 * @see #getStandingTicksRatio()
	 * @see #isStandingUp()
	 **/
	public boolean canWalk() {
		return this.isStandingUp() && this.getStandingTicks() >= MAX_STANDING_TICKS;
	}
	
	/**
	 * If entity is starting to stand up, adds to the current
	 * standing ticks;
	 * If entity is starting to sit down, takes away from the
	 * current standing ticks
	 * @param toAdd the amount of ticks to add or subtract
	 **/
	public void updateStandingTicks(final int toAdd) {
		final boolean isStandingUp = this.isStandingUp();
		final int standingTicks = this.getStandingTicks();
		if(isStandingUp && standingTicks < MAX_STANDING_TICKS) {
			addStandingTicks(toAdd);
		} else if(!isStandingUp && standingTicks > 0) {
			addStandingTicks(-toAdd);
		}
	}
	
	/**
	 * Increments (or decrements) the STANDING_TICKS field
	 * @param toAdd the amount to add (may be negative)
	 **/
	public void addStandingTicks(final int toAdd) {
		if(toAdd != 0) {
			setStandingTicks(this.getStandingTicks() + toAdd);
		}
	}
	
	public void setStandingTicks(final int toSet) {
		if(toSet != this.getStandingTicks()) {
			this.getDataManager().set(STANDING_TICKS, (byte)toSet);
		}
	}
	
	/**
	 * @param standingUp whether the entity is starting to stand up
	 **/
	public void setStandingState(final boolean standingUp) {
		if(standingUp != this.isStandingUp()) {
			this.getDataManager().set(IS_STANDING_UP, standingUp);
		}
	}
	
	/**
	 * @param isTargetLooking whether the entity's target is looking
	 * at this entity
	 **/
	public void setTargetLooking(final boolean isTargetLooking) {
		if(isTargetLooking != this.isTargetLooking()) {
			this.getDataManager().set(IS_TARGET_LOOKING, isTargetLooking);
		}
	}
	
	/**
	 * @return the percent amount of "standing up" that the mob
	 * has currently attained
	 **/
	public float getStandingTicksRatio() {
		return (float)getStandingTicks() / (float)MAX_STANDING_TICKS;
	}
	
	/**
	 * @return the number of ticks since/until entity is sitting
	 **/
	public int getStandingTicks() {
		return this.getDataManager().get(STANDING_TICKS).intValue();
	}
	
	/**
	 * @return true if the entity is starting to get up, 
	 * false if the entity is starting to sit down
	 **/
	public boolean isStandingUp() {
		return this.getDataManager().get(IS_STANDING_UP).booleanValue();
	}
	
	/**
	 * @return True if the entity's target is looking toward the entity
	 **/
	public boolean isTargetLooking() {
		return this.getDataManager().get(IS_TARGET_LOOKING).booleanValue();
	}
	
	class StandingSittingGoal extends Goal {
		private final PossessedPumpkin entity;
		
		public StandingSittingGoal(final PossessedPumpkin possessedPumpkin) {
			this.entity = possessedPumpkin;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
		}
		
		@Override
		public boolean shouldExecute() {
			return true;
		}

		@Override
		public void startExecuting() {
			tick();
		}

		@Override
		public boolean shouldContinueExecuting() {
			return shouldStandUp() != entity.isStandingUp();
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		@Override
		public void tick() {
			if(entity.getAttackTarget() != null) {
				// update 'isTargetLooking'
				entity.setTargetLooking(entity.canBeSeen(entity.getAttackTarget()));
				// update 'standingState' based on whether target is looking OR too close
				if(shouldStandUp()) {
					// if target is too close, immediately stand
					entity.setStandingState(true);
					// extra update to stand up twice as fast
					entity.updateStandingTicks(1);
				} else {
					entity.setStandingState(false);
					entity.hide();
				}				
			} else {
				// stand up and wander around
				entity.setStandingState(true);
			}
			
			// update standing ticks
			entity.updateStandingTicks(1);
		}
		
		private boolean shouldStandUp() {
			if(entity.getAttackTarget() != null) {
				// if target is looking, only stand if target is too close
				if(entity.canBeSeen(entity.getAttackTarget())) {
					final double disSq = entity.getDistanceSq(entity.getAttackTarget());
					final double attackRangeSq = Math.pow(3.0D, 2);
					return disSq < attackRangeSq;
				}
			}
			return true;
		}
	}
	
	class MoveTowardTargetGoal extends Goal {
		
		private final PossessedPumpkin entity;
		private final double speed;

		public MoveTowardTargetGoal(final PossessedPumpkin entityIn, final double speedIn) {
			this.entity = entityIn;
			this.speed = speedIn;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean shouldExecute() {
			return this.entity.canWalk() && entity.getAttackTarget() != null && entity.getNavigator().noPath();
		}
		
		@Override
		public boolean shouldContinueExecuting() {
			return shouldExecute();
		}

		@Override
		public void startExecuting() {
			if(this.entity.getAttackTarget() != null) {
				this.entity.getNavigator().tryMoveToEntityLiving(entity.getAttackTarget(), speed);
			}
		}
		
	}

}
