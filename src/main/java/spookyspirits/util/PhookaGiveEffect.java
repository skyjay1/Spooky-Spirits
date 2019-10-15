package spookyspirits.util;

import java.util.function.Consumer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;

public class PhookaGiveEffect implements Consumer<PlayerEntity> {
	
	private final EffectInstance effect;
	
	public PhookaGiveEffect(final EffectInstance e) {
		effect = e;
	}
	
	@Override
	public void accept(PlayerEntity t) {
		t.addPotionEffect(effect);
	}
}
