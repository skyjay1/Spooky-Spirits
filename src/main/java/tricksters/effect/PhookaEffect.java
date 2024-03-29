package tricksters.effect;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import tricksters.init.Tricksters;

public abstract class PhookaEffect extends Effect {

	public PhookaEffect(final boolean isBlessing) {
		super(isBlessing ? EffectType.BENEFICIAL : EffectType.HARMFUL, isBlessing ? 0xffff66 : 0x996600);
	}

	protected abstract void tick(final LivingEntity entity, final int amp);

	protected boolean shouldRender() {
		return true;
	}

	@Override
	public void performEffect(final LivingEntity entity, final int amplifier) {
		if(entity.isServerWorld() && !entity.getEntityWorld().isRemote) {
			tick(entity, amplifier);
		}
	}

	@Override
	public boolean isReady(final int duration, final int amplifier) {
		return duration > 0;
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return new ArrayList<>();
	}

	@Override
	public boolean shouldRender(final EffectInstance instance) {
		return shouldRender();
	}

	@Override
	public boolean shouldRenderInvText(final EffectInstance instance) {
		return shouldRender();
	}

	@Override
	public boolean shouldRenderHUD(final EffectInstance instance) {
		return shouldRender();
	}

	public static class Footsteps extends PhookaEffect {
		public static final String NAME = "phooka_curse_footsteps";
		public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Tricksters.MODID, NAME);
		
		public Footsteps() {
			super(false);
			this.setRegistryName(Tricksters.MODID, NAME);
		}

		@Override
		protected void tick(final LivingEntity entity, final int amp) {
		}
		
		@Override
		protected boolean shouldRender() {
			return false;
		}
	}

	public static class Invisibility extends PhookaEffect {
		public static final String NAME = "phooka_blessing_invisibility";
		public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Tricksters.MODID, NAME);

		public Invisibility() {
			super(true);
			this.setRegistryName(REGISTRY_NAME);
		}

		@Override
		public void tick(final LivingEntity entity, final int amp) {
			if (entity.isServerWorld() && entity.ticksExisted % 2 == 0) {
				final BlockPos pos = entity.getPosition();
				final int light = entity.getEntityWorld().getLight(pos);
				final EffectInstance invis = entity.getActivePotionEffect(Effects.INVISIBILITY);
				// if entity does not have full invisibility and the light level is low enough
				if ((invis == null || invis.getDuration() < 100) && light < 6 + amp) {
					entity.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 100, 0));
				}
//
//				if (entity.ticksExisted % 40 == 0)
//					System.out.println("Ticking! Pos = " + pos + ", Light = " + light);
			}

		}
	}

	public static class Sponge extends PhookaEffect {
		public static final String NAME = "phooka_curse_sponge";
		public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Tricksters.MODID, NAME);

		public Sponge() {
			super(false);
			this.setRegistryName(REGISTRY_NAME);
		}

		@Override
		public void tick(final LivingEntity entity, final int amp) {
			if (entity.ticksExisted % 4 == 0 && entity.isInWater()) {
				// place sponge
				entity.getEntityWorld().setBlockState(entity.getPosition(), Blocks.SPONGE.getDefaultState());
				// remove effect
				entity.removePotionEffect(this);
			}
		}

		@Override
		public boolean shouldRender() {
			return false;
		}
	}

	public static class Eggs extends PhookaEffect {
		public static final String NAME = "phooka_curse_eggs";
		public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Tricksters.MODID, NAME);
		public static final int INTERVAL = 20;
		private static final int MIN_RANGE = 2;
		private static final int MAX_RANGE = 5;

		public Eggs() {
			super(false);
			this.setRegistryName(REGISTRY_NAME);
		}

		@Override
		public boolean shouldRender() {
			return false;
		}

		@Override
		protected void tick(final LivingEntity entity, final int amp) {
			// throw eggs at the player >:D
			if (entity.ticksExisted % INTERVAL == 0) {
				final Vec3d origin = entity.getPositionVec().add(0, entity.getHeight() / 2, 0);
				// get a random nearby position
				for (int attempts = 30; attempts > 0; attempts--) {
					int x = MIN_RANGE + entity.getRNG().nextInt(MAX_RANGE - MIN_RANGE);
					int y = entity.getRNG().nextInt(MIN_RANGE);
					int z = MIN_RANGE + entity.getRNG().nextInt(MAX_RANGE - MIN_RANGE);
					final Vec3d p = origin.add(
							entity.getRNG().nextBoolean() ? x : -x, 
							y,
							entity.getRNG().nextBoolean() ? z : -z);
					// if that position is traceable to the player, it will work
					RayTraceResult.Type result = entity.world.rayTraceBlocks(new RayTraceContext(origin, p,
							RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity)).getType();
					if (result == RayTraceResult.Type.MISS) {
						// spawn an egg
						final EggEntity egg = new EggEntity(entity.getEntityWorld(), p.x, p.y, p.z);
						double d0 = entity.posY + (double) entity.getEyeHeight() - (double) 1.1F;
						double d1 = entity.posX - p.getX();
						double d2 = d0 - egg.posY;
						double d3 = entity.posZ - p.getZ();
						float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
						egg.shoot(d1, d2 + (double) f, d3, 1.4F, 0.1F);
						entity.getEntityWorld().playSound(p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_EGG_THROW,
								SoundCategory.PLAYERS, 1.0F, 1.0F / (entity.getRNG().nextFloat() * 0.4F + 0.8F), false);
						entity.world.addEntity(egg);
						return;
					}
				}
			}
		}

	}
	
	public static class Squid extends PhookaEffect {
		public static final String NAME = "phooka_curse_squid";
		public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Tricksters.MODID, NAME);
		final double range;
		
		public Squid(final double rangeIn) {
			super(false);
			this.setRegistryName(REGISTRY_NAME);
			range = rangeIn;
		}
		
		public Squid() {
			this(1.0D);
		}
		
		@Override
		public boolean shouldRender() {
			return false;
		}

		@Override
		protected void tick(final LivingEntity entity, final int amp) {
			// find nearby squid to grab (it shouldn't be too far)
			final List<SquidEntity> list = entity.getEntityWorld().getEntitiesWithinAABB(SquidEntity.class, entity.getBoundingBox().grow(range));
			if(!list.isEmpty() && list.get(0).isAlive()) {
				// grab the first squid and place it at player head position
				final SquidEntity sq = list.get(0);
				final double x = entity.posX;
				final double y = entity.posY + entity.getEyeHeight() - (sq.getHeight() / 2);
				final double z = entity.posZ;
				sq.setPosition(x, y, z);
				sq.setBoundingBox(entity.getBoundingBox().shrink(0.01D));
				//sq.setMotion(entity.getMotion().mul(1.1D, 0.01D, 1.1D));
				// stop the squid from suffocating or being killed
				sq.heal(0.1F);
				sq.setAir(300);
			}
		}
		
	}
}
