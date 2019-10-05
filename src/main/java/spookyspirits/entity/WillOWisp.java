package spookyspirits.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import spookyspirits.block.BlockWispLight;
import spookyspirits.init.SpiritBlocks;

public class WillOWisp extends FlyingEntity {

	protected static final DataParameter<Optional<UUID>> WISP_UUID = EntityDataManager
			.createKey(WillOWisp.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final String KEY_WISP = "WispUUID";
	
	private float fadeFactor;

	public WillOWisp(EntityType<? extends FlyingEntity> type, World world) {
		super(type, world);
		
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(WISP_UUID, Optional.empty());
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new PlaceLightGoal(this, 6));
		this.goalSelector.addGoal(2, new MoveToWispGoal(this, 8.0D));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}

	@Override
	public void livingTick() {
		super.livingTick();
		
		// udpate fade factor
		this.fadeFactor = getFadeFactor(this.ticksExisted, 0.08F, 1.55F);
		
		// if no wisp after fully loading, despawn
		if(this.ticksExisted % 30 == 15 && this.isServerWorld() && !this.world.isRemote && !this.hasWisp()) {
			this.remove();
		}
		
		// spawn particles
		if(this.world.isRemote && this.world.getRandom().nextInt(5) == 0) {
			world.addParticle(ParticleTypes.ENCHANT, 
					this.posX + world.rand.nextDouble() - 0.5D,
					this.posY + world.rand.nextDouble() - 0.5D, 
					this.posZ + world.rand.nextDouble() - 0.5D,
					world.rand.nextDouble() * 0.1D - 0.05D,
					world.rand.nextDouble() * 0.2D - 0.1D,
					world.rand.nextDouble() * 0.1D - 0.05D);
		}
	}
	
	@Override
	public boolean canDespawn(double disToPlayer) {
		return !this.hasWisp();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public int getBrightnessForRender() {
		return (int) (15728880F);
	}

	@Override
	public float getBrightness() {
		return 1.0F;
	}
	
	@Override
	public boolean isPushedByWater() {
		return false;
	}
	
	@Override
	public void applyEntityCollision(Entity entityIn) {
		// do nothing
	}
	
	public void setWisp(final UUID wispIn) {
		this.getDataManager().set(WISP_UUID, Optional.of(wispIn));
	}
	
	/**
	 * Checks whether there is a UUID stored AND if the entity
	 * actually still exists AND if it's still alive
	 * @return
	 **/
	public boolean hasWisp() {
		return getWispUUID() != null && getWisp() != null && getWisp().isAlive();
	}
	
	@Nullable
	public UUID getWispUUID() {
		return this.getDataManager().get(WISP_UUID).orElse(null);
	}

	@Nullable
	public Wisp getWisp() {
		final UUID uuid = getWispUUID();
		return uuid != null && this.world instanceof ServerWorld
				? (Wisp)((ServerWorld) this.world).getEntityByUuid(uuid)
				: null;
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		final UUID uuid = this.getWispUUID();
		if(uuid != null) {
			compound.putUniqueId(KEY_WISP, uuid);
		}

	}

	@Override
	public void readAdditional(final CompoundNBT compound) {
		super.readAdditional(compound);
		if(compound.hasUniqueId(KEY_WISP)) {
			this.setWisp(compound.getUniqueId(KEY_WISP));
		}
	}
	
	/**
	 * @param ticks the number of ticks elapsed since beginning
	 * @param fadeSpeed affects the amount of change in alpha per tick
	 * @param amplitude affects the amount of time spent fully transparent or fully opaque
	 * @return a number between 0.0F and 0.95F, where 0.0F is fully transparent
	 **/
	public static float getFadeFactor(final int ticks, final float fadeSpeed, final float amplitude) {
		// downshift: fade factor will be shifted DOWN by 4%
		float downShift = amplitude * 0.04F;
		// fade: a number between (-amplitude) and (amplitude), with (downshift) subtracted
		float fade = (float)Math.sin(ticks * fadeSpeed) * amplitude - downShift;
		// return a single float between 0 and 0.95 (time spent outside of these bounds is clamped)
		return MathHelper.clamp(fade, 0.0F, 0.95F);
	}
	
	class PlaceLightGoal extends Goal {
		private final WillOWisp willowisp;
		private final BlockState state;
				
		public PlaceLightGoal(final WillOWisp willowispIn, final int lightLevel) {
			willowisp = willowispIn;
			state = SpiritBlocks.WISP_LIGHT.getDefaultState()
					.with(BlockWispLight.LIGHT_LEVEL, lightLevel);
		}

		@Override
		public boolean shouldExecute() {
			return willowisp.ticksExisted % BlockWispLight.DEF_TICK_RATE == 0;
		}
		
		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}
		
		@Override
		public void startExecuting() {
			final BlockPos pos = getPlaceablePos(willowisp.getPosition());
			if(pos != null) {
				final boolean waterlogged = willowisp.getEntityWorld().hasWater(pos);
				willowisp.getEntityWorld().setBlockState(pos, 
						state.with(BlockStateProperties.WATERLOGGED, waterlogged), 2);
			}
		}
		
		@Nullable
		private BlockPos getPlaceablePos(final BlockPos origin) {
			final int[] ia = { 0, -1, 1 };
			for(int x : ia) {
				for(int y : ia) {
					for(int z : ia) {
						final BlockPos p = origin.add(x, y, z);
						// if it's already a light, don't do anything else
						if(willowisp.getEntityWorld().getBlockState(p).getBlock() == SpiritBlocks.WISP_LIGHT) {
							return null;
						} else if(isReplaceablePos(p)) {
							// if it's not a light but it is replaceable, it's a valid position
							return p;
						}
					}
				}
			}
			
			return null;
		}
		
		private boolean isReplaceablePos(final BlockPos pos) {
			final BlockState s = willowisp.getEntityWorld().getBlockState(pos);
			return willowisp.getEntityWorld().isAirBlock(pos) || 
					(s.getBlock() == Blocks.WATER && s.get(FlowingFluidBlock.LEVEL) == 0);
		}
	}
	
	class MoveToWispGoal extends Goal {

		private final WillOWisp willowisp;
		private final double range;
		
		public MoveToWispGoal(final WillOWisp willowispIn, final double detectionRadius) {
			this.willowisp = willowispIn;
			this.range = detectionRadius;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		@Override
		public boolean shouldExecute() {
			return this.willowisp.getWisp() != null && isPlayerClose() && this.willowisp.fadeFactor > 0.9D;
		}
		
		@Override
		public void startExecuting() {
			final Wisp wisp = this.willowisp.getWisp();
			final BlockPos wispPos = wisp.getPosition();
			final BlockPos origin = willowisp.getPosition();
			final double curDisSq = wispPos.distanceSq(origin);
			final int radius = Math.max(2, (int)Math.ceil(range * 2));
			final int radDiv2 = radius / 2;
			// attempt to find a blockpos that is AIR and CLOSER (to the wisp)
			BlockPos pos;
			for(int i = 0, attempts = 30; i < attempts; i++) {
				int x = willowisp.rand.nextInt(radius) - radDiv2;
				int y = willowisp.rand.nextInt(radDiv2);
				int z = willowisp.rand.nextInt(radius) - radDiv2;
				int dy = 1 + willowisp.rand.nextInt(3);
				pos = getBestY(origin.add(x, y, z), dy);
				if(wispPos.distanceSq(pos) < curDisSq && willowisp.getEntityWorld().isAirBlock(pos)) {
					// attempt to teleport the willowisp
					if(willowisp.attemptTeleport(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, false)) {
						willowisp.moveToBlockPosAndAngles(pos, willowisp.rotationYaw, willowisp.rotationPitch);
						return;
					}
				}
			}
			
		}
		
		/** 
		 * Tries to find the lowest y-value that is still air 
		 * @param p origin (does not affect x and z)
		 * @param dy the amount of change in y per test
		 **/
		private BlockPos getBestY(BlockPos p, int dy) {
			int attempts = 6;
			dy = Math.max(1, dy);
			while(willowisp.getEntityWorld().isAirBlock(p.down(dy)) && attempts-- > 0) {
				p = p.down(dy);
			}
			return p;
		}

		private boolean isPlayerClose() {
			return getClosestPlayer(range) != null;
		} 
		
		/**
		 * @param range the radius within which to search
		 * @return the closest player within range, or null
		 **/
		@Nullable
		private PlayerEntity getClosestPlayer(final double range) {
			List<PlayerEntity> list = willowisp.getEntityWorld()
					.getEntitiesWithinAABB(PlayerEntity.class, 
							willowisp.getBoundingBox().grow(range, range, range));
			
			// find closest player
			double closest = range * range;
			PlayerEntity closestPlayer = null;
			if(!list.isEmpty()) {
				for(final PlayerEntity e : list) {
					double d = willowisp.getDistanceSq(e);
					if(d < closest) {
						closest = d;
						closestPlayer = e;
					}
				}
			}
			return closestPlayer;
		}

	}

}
