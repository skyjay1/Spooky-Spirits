package spookyspirits.effect;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public abstract class PhookaEffect extends Effect {

	public PhookaEffect(final boolean isBlessing) {
		super(isBlessing ? EffectType.BENEFICIAL : EffectType.HARMFUL, isBlessing ? 0xffff66 : 0x996600);
	}
	
	protected abstract void tick(final LivingEntity entity, final int amp);
	protected boolean shouldRender() { return true; }
	
	@Override
	public void performEffect(final LivingEntity entity, final int amplifier) {
		tick(entity, amplifier);
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
	
	public static class Generic extends PhookaEffect {
		public Generic(boolean isBlessing) {
			super(isBlessing);
		}

		@Override
		protected void tick(final LivingEntity entity, final int amp) { }
	}
	
	public static class Invisibility extends PhookaEffect {
		public static final String NAME = "phooka_blessing_invisibility";
		public Invisibility() {
			super(true);
		}
		
		@Override
		public void tick(final LivingEntity entity, final int amp) {
			if(entity.ticksExisted % 5 == 0) {
				final BlockPos pos = entity.getPosition();
				final int light = entity.getEntityWorld().getLight(pos);
				
				if(light < 7 + amp) {
					entity.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 10, 0));
					// DEBUG
					System.out.println("Applying Invisibility");
				}
				
				if(entity.ticksExisted % 40 == 0)
					System.out.println("Ticking! Pos = " + pos + ", Light = " + light);
			}
			
			
		}
	}
	
	public static class Sponge extends PhookaEffect {
		public static final String NAME = "phooka_curse_sponge";
		public Sponge() {
			super(false);
		}
		
		@Override
		public void tick(final LivingEntity entity, final int amp) {
			if(entity.isInWater()) {
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
}
