package spookyspirits.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import spookyspirits.entity.goal.PlaceLightGoal;

public class WillOWispEntity extends FlyingEntity implements ILightEntity {

	protected static final DataParameter<Optional<UUID>> WISP_UUID = EntityDataManager
			.createKey(WillOWispEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final String KEY_WISP = "WispUUID";
	
	public WillOWispEntity(EntityType<? extends FlyingEntity> type, World world) {
		super(type, world);
		this.moveController = new WillOWispEntity.MoveHelperController(this);
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(WISP_UUID, Optional.empty());
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new PlaceLightGoal(this, getLightLevel()));
		this.goalSelector.addGoal(2, new MoveToWispGoal(this, 5.5D, 1.0D));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.19D);
	}

	@Override
	public void livingTick() {
		super.livingTick();
		
		// every 30 ticks, check if has no wisp OR is close enough to wisp to despawn
		if(this.ticksExisted % 30 == 15 && this.isServerWorld() && !this.world.isRemote) {
			if(!this.hasWisp() || (this.rand.nextBoolean() && this.getDistanceSq(this.getWisp()) < Math.pow(3.0D, 2))) {
				this.remove();
			}
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
	public void collideWithEntity(final Entity entityIn) {
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
	public WispEntity getWisp() {
		final UUID uuid = getWispUUID();
		return uuid != null && this.world instanceof ServerWorld
				? (WispEntity)((ServerWorld) this.world).getEntityByUuid(uuid)
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
	
	@Override
	public int getLightLevel() {
		return 6;
	}

	static class MoveHelperController extends MovementController {
		private final WillOWispEntity entity;
		private int courseChangeCooldown;

		public MoveHelperController(WillOWispEntity willowisp) {
			super(willowisp);
			this.entity = willowisp;
		}

		@Override
		public void tick() {
			if (this.action == MovementController.Action.MOVE_TO) {
				if (this.courseChangeCooldown-- <= 0) {
					this.courseChangeCooldown += this.entity.getRNG().nextInt(5) + 12;
					Vec3d vec3d = new Vec3d(this.posX - this.entity.posX, this.posY - this.entity.posY,
							this.posZ - this.entity.posZ);
					double d0 = vec3d.length();
					vec3d = vec3d.normalize();
					double speed = this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() * this.getSpeed();
					if (this.hasClearPath(vec3d, MathHelper.ceil(d0))) {
						if(d0 < 1.0D) {
							speed = 0.005D * d0;
						}
						this.entity.setMotion(this.entity.getMotion().add(vec3d.scale(speed)));
					} else {
						this.action = MovementController.Action.WAIT;
					}
				}
			}
		}

		private boolean hasClearPath(final Vec3d distanceToTarget, final int steps) {
			AxisAlignedBB axisalignedbb = this.entity.getBoundingBox();
			for (int i = 1; i < steps; ++i) {
				axisalignedbb = axisalignedbb.offset(distanceToTarget);
				if (!this.entity.world.isCollisionBoxesEmpty(this.entity, axisalignedbb)) {
					return false;
				}
			}

			return true;
		}
	}

	class MoveToWispGoal extends Goal {

		private final WillOWispEntity willowisp;
		private final double range;
		private final double speedFactor;
		
		public MoveToWispGoal(final WillOWispEntity willowispIn, final double detectionRadius, final double speed) {
			this.willowisp = willowispIn;
			this.range = detectionRadius;
			this.speedFactor = speed;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		@Override
		public boolean shouldExecute() {
			return this.willowisp.getWisp() != null && isPlayerClose();
		}
		
		@Override
		public void startExecuting() {
			tick();
		}
		
		@Override
		public void tick() {
			// only execute half the time (reduce lag)
			if(this.willowisp.ticksExisted % 2 == 0) {
				return;
			}
			// choose a random position within range and attempt to move there
			final WispEntity wispEntity = this.willowisp.getWisp();
			final BlockPos wispPos = wispEntity.getPosition();
			//final Vec3d currentPos = wispEntity.getPositionVec().add(0, 0.5D, 0);
			final BlockPos origin = willowisp.getPosition();
			final double curDisSq = wispPos.distanceSq(origin);
			final int radius = Math.max(2, (int)Math.ceil(range * 1.5D));
			final int radDiv2 = radius / 2;
			// attempt to find a blockpos that is AIR and CLOSER (to the wisp)
			BlockPos pos;
			//Vec3d target;
			for(int i = 0, attempts = 20; i < attempts; i++) {
				int x = willowisp.rand.nextInt(radius) - radDiv2;
				int y = willowisp.rand.nextInt(radDiv2);
				int z = willowisp.rand.nextInt(radius) - radDiv2;
				int dy = 1 + willowisp.rand.nextInt(3);
				pos = WispEntity.getBestY(willowisp.getEntityWorld(), origin.add(x, y, z), dy);
				//target = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
				//RayTraceResult.Type result = willowisp.getEntityWorld().rayTraceBlocks(new RayTraceContext(currentPos, target,
				//		RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, willowisp)).getType();
				if(wispPos.distanceSq(pos) < curDisSq && willowisp.getEntityWorld().isAirBlock(pos)) {
					// attempt to move
					willowisp.getMoveHelper().setMoveTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, speedFactor);
					return;
				}
			}
			
		}

		private boolean isPlayerClose() {
			final List<PlayerEntity> list = willowisp.getEntityWorld()
					.getEntitiesWithinAABB(PlayerEntity.class, 
							willowisp.getBoundingBox().grow(range));
			return !list.isEmpty();
		} 
		
		/**
		 * @param range the radius within which to search
		 * @return the closest player within range, or null
		 **/
		@Nullable
		private PlayerEntity getClosestPlayer() {
			final List<PlayerEntity> list = willowisp.getEntityWorld()
					.getEntitiesWithinAABB(PlayerEntity.class, 
							willowisp.getBoundingBox().grow(range));
			
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
