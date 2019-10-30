package tricksters.entity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tricksters.client.gui.GuiLoader;
import tricksters.init.ModObjects;
import tricksters.util.PhookaRiddle;
import tricksters.util.PhookaRiddles;

/*
 * Desc: 
 * [ ] PhookaEntity would spawn in all forest type biomes using Forge's BiomeDict. 
 * [X] it could turn any berries laying on the ground or growing on a bush into spoiled berries. 
 * [X] wherever the phooka spawned, it would sit waiting for the playing to interact with it. 
 * [X] interacting with a phooka brings up a GUI which is a riddle minigame, 
 * [X] it selects from several pre-set riddles, you choose from multiple answers
 * [X] depending on the nature of the riddle, the phooka would bless you with certain effects 
 * which would last for a few days, 
 * [X] it would also remove any curses blighted upon the player no matter what riddle the player solves. 
 * [X] however, if the player puts in the wrong answer to the riddle, or attacks the phooka, 
 * the phooka would curse the player. 
 * [X] curses could range from anything to blindness debuffs, to monsters spawning more 
 * frequently and closer to the player.
 */
public class PhookaEntity extends MonsterEntity {
	
	// Despawning Ticks: 0 means NOT despawning, 1 to [MAX] means starting to despawn
	private static final DataParameter<Byte> DESPAWNING_TICKS = EntityDataManager.createKey(PhookaEntity.class, DataSerializers.BYTE);
	private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(PhookaEntity.class, DataSerializers.BOOLEAN);

	private static final String KEY_DESPAWNING_TICKS = "DespawningTicks";
	private static final String KEY_SITTING = "Sitting";
	
	private static final int MAX_DESPAWNING_TICKS = 50;
	
	public PhookaEntity(final EntityType<? extends MonsterEntity> type, final World world) {
		super(type, world);
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0D, 9.0F));
		this.goalSelector.addGoal(3, new SittingGoal(this, 1200, 750));
		this.goalSelector.addGoal(4, new WanderAvoidWaterGoal(this, 0.7D));
		this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(7, new SpoilBerriesGoal(this, rand.nextInt(3) + 3));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.29D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(DESPAWNING_TICKS, Byte.valueOf((byte)0));
		this.getDataManager().register(SITTING, Boolean.valueOf(false));
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		if(this.isServerWorld() && !this.world.isRemote) {
			// if the entity is despawning, increment despawning ticks until it's gone
			if(this.isDespawning()) {
				this.setDespawningTicks(this.getDespawningTicks() + 1);
				// if despawning ticks reaches max, despawn
				if(this.getDespawningTicks() > MAX_DESPAWNING_TICKS) {
					this.remove();
				}
			}
		} else {
			// spawn particles
			if(this.isDespawning() && this.getDespawningTicks() > MAX_DESPAWNING_TICKS / 2) {
				this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, 
						this.posX + rand.nextDouble() * 0.5D - 0.25D, 
						this.posY + 0.25D + rand.nextDouble() * 1.25D, 
						this.posZ + rand.nextDouble() * 0.5D - 0.25D, 
						rand.nextDouble() * 0.1D - 0.05D,
						rand.nextDouble() * 0.35D,
						rand.nextDouble() * 0.1D - 0.05D);
			}
		}
	}

	@Override
	public EntitySize getSize(final Pose poseIn) {
		return isSitting() ? super.getSize(poseIn).scale(1.1F, 0.7F) : super.getSize(poseIn);
	}
	
	@Override
	public void damageEntity(final DamageSource source, final float amount) {
		if(!this.isInvulnerableTo(source)) {
			super.damageEntity(source, amount);
			this.setSitting(false);
		}
	}
	
	@Override
	public boolean isInvulnerableTo(final DamageSource src) {
		return src == DamageSource.SWEET_BERRY_BUSH || super.isInvulnerableTo(src);
	}
	
	@Override
	public void onDeath(final DamageSource source) {
		if(source.getTrueSource() instanceof PlayerEntity) {
			final PlayerEntity p = (PlayerEntity)source.getTrueSource();
			// apply a curse
			final PhookaRiddle r = PhookaRiddles.getRandom(rand);
			r.getCursing().accept(p);
		}
	}
	
	public boolean isSitting() {
		return this.getDataManager().get(SITTING);
	}
	
	public void setSitting(final boolean isSitting) {
		if(isSitting != this.getDataManager().get(SITTING)) {
			this.getDataManager().set(SITTING, isSitting);
			if(isSitting) {
				this.getNavigator().clearPath();
				this.setAttackTarget(null);
			}
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public float getFadeFactor() {
		final float FADE_AT = MAX_DESPAWNING_TICKS / 4;
		if(this.getDespawningTicks() < FADE_AT) {
			return 1.0F;
		}
		final float ratio = (this.getDespawningTicks() - FADE_AT) / (MAX_DESPAWNING_TICKS - FADE_AT);
		return 1.0F - ratio;
	}
	
	@Override
	public boolean processInteract(final PlayerEntity player, final Hand hand) {
		if(player.getEntityWorld().isRemote && this.getDespawningTicks() <= 0 && this.isSitting() && player.getHeldItem(hand).isEmpty()) {
			final PhookaRiddle riddle = getRiddleFor(player);
			if(riddle != null) {
				GuiLoader.loadPhookaGui(this, player, riddle);
			}
			return true;
		}
		return super.processInteract(player, hand);
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putByte(KEY_DESPAWNING_TICKS, (byte)this.getDespawningTicks());
		compound.putBoolean(KEY_SITTING, this.isSitting());
	}

	@Override
	public void readAdditional(final CompoundNBT compound) {
		super.readAdditional(compound);
		this.setDespawningTicks(compound.getByte(KEY_DESPAWNING_TICKS));
		this.setSitting(compound.getBoolean(KEY_SITTING));
	}
	
	/**
	 * Updates the number of ticks since entity
	 * started despawning. Set to 1 to begin despawning
	 * @param toSet the new value
	 **/
	public void setDespawningTicks(final int toSet) {
		if(toSet != this.getDespawningTicks()) {
			this.getDataManager().set(DESPAWNING_TICKS, (byte)toSet);
		}
	}
	
	/**
	 * @return the number of ticks since entity started despawning
	 **/
	public int getDespawningTicks() {
		return this.getDataManager().get(DESPAWNING_TICKS).intValue();
	}
	
	/**
	 * @return true if the entity has started despawning
	 **/
	public boolean isDespawning() {
		return getDespawningTicks() > 0;
	}
	
	@Nullable
	private static PhookaRiddle getRiddleFor(final PlayerEntity player) {
		return PhookaRiddles.getByName("pen");
		// TODO finish testing one by one
		//return PhookaRiddles.getRandom(player.getRNG());
	}

	public static boolean canSpawnHere(EntityType<PhookaEntity> entity, IWorld world, SpawnReason reason,
			BlockPos pos, Random rand) {
		// TODO
		return true;
	}
	
	/**
	 * Adapted to only wander around while entity is fully standing up
	 **/
	class WanderAvoidWaterGoal extends WaterAvoidingRandomWalkingGoal {

		private final PhookaEntity entity;
		public WanderAvoidWaterGoal(final PhookaEntity creature, final double speedFactorIn) {
			super(creature, speedFactorIn);
			this.entity = creature;
		}

		@Override
		public boolean shouldExecute() {
			return !entity.isSitting() && super.shouldExecute();
		}
	}
	
	class SpoilBerriesGoal extends Goal {
		
		private final Entity entity;
		private final int range;
		
		protected SpoilBerriesGoal(final Entity entityIn, final int rangeIn) {
			entity = entityIn;
			range = rangeIn;
		}

		@Override
		public boolean shouldExecute() {
			return entity.getEntityWorld().getRandom().nextInt(30) == 0;
		}
		
		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}
		
		@Override
		public void startExecuting() {
			// search for nearby berry bush BLOCKS
			final int rangeHalf = range / 2;
			final BlockPos origin = entity.getPosition();
			for(int x = -range; x <= range; x++) {
				for(int y = -rangeHalf; y <= rangeHalf; y++) {
					for(int z = -range; z <= range; z++) {
						final BlockPos p = origin.add(x, y, z);
						final BlockState s = entity.getEntityWorld().getBlockState(p);
						// check if it's a sweet berry bush
						if(s.getBlock() == Blocks.SWEET_BERRY_BUSH) {
							// replace with 'spoiled' berry bush (with same age)
							int age = s.get(SweetBerryBushBlock.AGE).intValue();
							entity.getEntityWorld().setBlockState(p,
									ModObjects.SPOILED_BERRY_BUSH.getDefaultState()
									.with(SweetBerryBushBlock.AGE, Integer.valueOf(age)), 2);
						}
					}
				}
			}
			// search for nearby berry ITEMS
			final List<ItemEntity> list = entity.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, 
					entity.getBoundingBox().grow((double)range), i -> i.getItem().getItem() == Items.SWEET_BERRIES);
			for(final ItemEntity i : list) {
				final int size = i.getItem().getCount();
				i.setItem(new ItemStack(ModObjects.SPOILED_BERRIES, size));
			}
		}
	}
	
	class SittingGoal extends Goal {
		
		private final PhookaEntity entity;
		private final int sittingTime;
		private final int walkingTime;
		private int timeUntilChange;
		
		public SittingGoal(final PhookaEntity creature, final int timeSitting, final int timeWalking) {
			this.entity = creature;
			timeUntilChange = Math.max(timeSitting, timeWalking);
			sittingTime = timeSitting;
			walkingTime = timeWalking;
		}
		
		@Override
		public boolean shouldExecute() {
			return timeUntilChange-- < 0;
		}
		
		@Override
		public void startExecuting() {
			if(entity.isSitting()) {
				// if entity is sitting down, stand up instead
				timeUntilChange = walkingTime + entity.getRNG().nextInt(walkingTime);
				entity.setSitting(false);
			} else if(entity.getAttackTarget() == null) {
				// if entity is not attacking anything, sit down
				timeUntilChange = sittingTime + entity.getRNG().nextInt(sittingTime);
				entity.setSitting(true);
			}
		}
	}

}
