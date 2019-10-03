package spookyspirits.entity;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WillOWisp extends FlyingEntity {
	
	private Wisp wisp;

	public WillOWisp(EntityType<? extends FlyingEntity> type, World world) {
		super(type, world);
		
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		// TODO if player is near, move
		
		
		// spawn particles
		if(this.world.isRemote && this.world.getRandom().nextInt(5) == 0) {
			//for(int i = 0, n = 4; i < n; i++) {
				world.addParticle(ParticleTypes.ENCHANT, 
						this.posX + world.rand.nextDouble() - 0.5D,
						this.posY + world.rand.nextDouble() - 0.5D, 
						this.posZ + world.rand.nextDouble() - 0.5D,
						world.rand.nextDouble() * 0.1D - 0.05D,
						world.rand.nextDouble() * 0.2D - 0.1D,
						world.rand.nextDouble() * 0.1D - 0.05D);
			//}
		}
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
	
	public void setWisp(final Wisp wispIn) {
		this.wisp = wispIn;
	}
	
	public Wisp getWisp() {
		return this.wisp;
	}
	
	class MoveToWispGoal extends Goal {

		private WillOWisp willowisp;
		private final double range;
		
		public MoveToWispGoal(final WillOWisp willowispIn, final double detectionRadius) {
			this.willowisp = willowispIn;
			this.range = detectionRadius;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		@Override
		public boolean shouldExecute() {
			return this.willowisp.getWisp() != null && isPlayerClose();
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
