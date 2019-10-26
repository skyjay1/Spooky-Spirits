package spookyspirits.entity;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PossessedPumpkinEntity extends MonsterEntity {
	
	private static final DataParameter<Byte> STANDING_TICKS = EntityDataManager.createKey(PossessedPumpkinEntity.class, DataSerializers.BYTE);
	private static final DataParameter<Boolean> IS_STANDING_UP = EntityDataManager.createKey(PossessedPumpkinEntity.class, DataSerializers.BOOLEAN);

	private static final String KEY_STANDING_TICKS = "StandingTicks";
	private static final String KEY_IS_STANDING_UP = "IsStandingUp";
	
	/** Must be less than or equal to Byte.MAX_VALUE [127] **/
	public static final int MAX_STANDING_TICKS = 12;
	
	private static final double attackDisSq = Math.pow(2.9D, 2);

	public PossessedPumpkinEntity(EntityType<? extends PossessedPumpkinEntity> type, World world) {
		super(type, world);
		this.stepHeight = 1.0F;
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(3, new HideGoal(this));
		this.goalSelector.addGoal(4, new AttackGoal(this, 1.0F, true));
		this.goalSelector.addGoal(5, new LeapAtTargetWhenCloseGoal(this, 0.35F));
		this.goalSelector.addGoal(7, new WanderAvoidWaterGoal(this, 0.76F));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 12.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.26F);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(STANDING_TICKS, Byte.valueOf((byte)0));
		this.getDataManager().register(IS_STANDING_UP, Boolean.valueOf(false));
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		// update whether pumpkinEntity should be standing
		if(this.isAlive() && this.isServerWorld() && !this.world.isRemote) {
			if(this.getAttackTarget() != null) {
				// if target exists, stand only if pumpkin cannot be seen by target
				// OR if the target is too close to the pumpkin
				final double disSq = this.getDistanceSq(this.getAttackTarget());
				final boolean canBeSeen = this.canBeSeen(this.getAttackTarget());
				this.setStandingState(!canBeSeen || disSq < attackDisSq);
			} else {
				// if no target at all, stand up to wander around
				this.setStandingState(true);
			}
			// update standing ticks
			this.updateStandingTicks(1);
		}
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putByte(KEY_STANDING_TICKS, (byte)this.getStandingTicks());
		compound.putBoolean(KEY_IS_STANDING_UP, this.isStandingUp());

	}

	@Override
	public void readAdditional(final CompoundNBT compound) {
		super.readAdditional(compound);
		this.setStandingTicks(compound.getByte(KEY_STANDING_TICKS));
		this.setStandingState(compound.getBoolean(KEY_IS_STANDING_UP));
	}
	
	/**
	 * Adapted from Enderman code. Detects if the player is facing
	 * towards the pumpkinEntity, with a bit of fuzz factor 
	 * @param pumpkinEntity the player (usually)
	 * @return true if the given pumpkinEntity is facing towards this pumpkinEntity
	 **/
	public boolean canBeSeen(final LivingEntity entity) {
		// higher fuzz = wider range of vectors
		final double fuzz = 2.0D;
		final double sightRangeSq = Math.pow(32D, 2);
		Vec3d vec3d = entity.getLook(1.0F).normalize();
		Vec3d vec3d1 = new Vec3d(this.posX - entity.posX, this.getBoundingBox().minY + (double) this.getEyeHeight()
				- (entity.posY + (double) entity.getEyeHeight()), this.posZ - entity.posZ);
		double d0 = vec3d1.length();
		vec3d1 = vec3d1.normalize();
		double d1 = vec3d.dotProduct(vec3d1);
		return d1 > fuzz / d0 && this.getDistanceSq(entity) < sightRangeSq && entity.canEntityBeSeen(this);
	}

	/**
	 * @return true if the pumpkinEntity's legs are completely ready to walk
	 * @see #getStandingTicksRatio()
	 * @see #isStandingUp()
	 **/
	public boolean canWalk() {
		return this.isStandingUp() && this.getStandingTicks() >= MAX_STANDING_TICKS;
	}
	
	/**
	 * If pumpkinEntity is starting to stand up, adds to the current
	 * standing ticks;
	 * If pumpkinEntity is starting to sit down, takes away from the
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
	 * Increments (or decrements) the STANDING_TICKS data
	 * @param toAdd the amount to add (may be negative)
	 **/
	public void addStandingTicks(final int toAdd) {
		if(toAdd != 0) {
			setStandingTicks(this.getStandingTicks() + toAdd);
		}
	}
	
	/**
	 * @param toSet the new value for standing ticks
	 **/
	public void setStandingTicks(final int toSet) {
		if(toSet != this.getStandingTicks()) {
			this.getDataManager().set(STANDING_TICKS, (byte)toSet);
		}
	}
	
	/**
	 * @param standingUp whether the pumpkinEntity is starting to stand up
	 **/
	public void setStandingState(final boolean standingUp) {
		if(standingUp != this.isStandingUp()) {
			this.getDataManager().set(IS_STANDING_UP, standingUp);
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
	 * @return the number of ticks since/until pumpkinEntity is sitting
	 **/
	public int getStandingTicks() {
		return this.getDataManager().get(STANDING_TICKS).intValue();
	}
	
	/**
	 * @return true if the pumpkinEntity is starting to get up, 
	 * false if the pumpkinEntity is starting to sit down
	 **/
	public boolean isStandingUp() {
		return this.getDataManager().get(IS_STANDING_UP).booleanValue();
	}
	
	public static boolean canSpawnHere(final EntityType<PossessedPumpkinEntity> entity, final IWorld world, final SpawnReason reason,
			final BlockPos pos, final Random rand) {
		System.out.println("checking canSpawnHere for possessed pumpkin! Possible spawn at " + pos);
		 // TODO
		return world.getDifficulty() != Difficulty.PEACEFUL && 
				world.canBlockSeeSky(pos) && 
				world.getBlockState(pos.down()).canEntitySpawn(world, pos.down(), entity);
	}
	
	/**
	 * Adapted to only wander around while entity is fully standing up
	 **/
	class WanderAvoidWaterGoal extends WaterAvoidingRandomWalkingGoal {

		private final PossessedPumpkinEntity entity;
		public WanderAvoidWaterGoal(final PossessedPumpkinEntity creature, final double speedFactorIn) {
			super(creature, speedFactorIn);
			this.entity = creature;
		}

		@Override
		public boolean shouldExecute() {
			return entity.canWalk() && super.shouldExecute();
		}
	}
	
	/**
	 * Adapted to update standing state when leaping
	 **/
	class LeapAtTargetWhenCloseGoal extends LeapAtTargetGoal {

		private final PossessedPumpkinEntity entity;
		public LeapAtTargetWhenCloseGoal(final PossessedPumpkinEntity leapingEntity, float leapMotionYIn) {
			super(leapingEntity, leapMotionYIn);
			this.entity = leapingEntity;
		}

		@Override
		public void startExecuting() {
			entity.setStandingState(true);
			entity.getJumpController().setJumping();
			super.startExecuting();
		}
	}
	
	/**
	 * Adapted to only look at target while entity is standing up
	 **/
	class HideGoal extends Goal {
		
		private final PossessedPumpkinEntity pumpkinEntity;

		public HideGoal(final PossessedPumpkinEntity entityIn) {
			pumpkinEntity = entityIn;
			this.setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
		}	

		@Override
		public boolean shouldExecute() {
			return !pumpkinEntity.isStandingUp();
		}
		
		@Override
		public void startExecuting() {
			hide();
		}
		
		/**
		 * Snaps the pumpkinEntity position to the nearest block,
		 * sets x and z motion to 0, and looks in a cardinal direction
		 **/
		public void hide() {
			moveToNearestBlock();
			lookStraightAhead();
			pumpkinEntity.getNavigator().clearPath();
		}
		
		/**
		 * Moves to the nearest block position and reduces motion
		 **/
		public void moveToNearestBlock() {
			final double x = Math.round(posX - 0.5D) + 0.5D;
			final double z = Math.round(posZ - 0.5D) + 0.5D;
			pumpkinEntity.setPosition(x, pumpkinEntity.posY, z);
			pumpkinEntity.setMotion(pumpkinEntity.getMotion().mul(0.01D, 1.0D, 0.01D));
			pumpkinEntity.markVelocityChanged();
		}
		
		/**
		 * Turns the entity to face in a cardinal direction
		 **/
		public void lookStraightAhead() {
			final float yaw = ((Math.round(pumpkinEntity.rotationYaw / 90F)) * 90F) % 360F;
			final float pitch = ((Math.round(pumpkinEntity.rotationPitch / 90F)) * 90F) % 360F;
			pumpkinEntity.prevRotationYaw = yaw;
			pumpkinEntity.prevRotationPitch = pitch;
			pumpkinEntity.setRotation(yaw, pitch);
		}
	}
	
	/**
	 * Adapted to only attack while entity is fully standing up
	 **/
	class AttackGoal extends MeleeAttackGoal {
		
		private final PossessedPumpkinEntity entity;
		
		public AttackGoal(final PossessedPumpkinEntity creature, final double speedIn, final boolean useLongMemory) {
			super(creature, speedIn, useLongMemory);
			this.entity = creature;
		}

		@Override
		public boolean shouldExecute() {
			return entity.canWalk() && super.shouldExecute();
		}
	}

}
