package spookyspirits.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import spookyspirits.init.ModObjects;

@EventBusSubscriber
public class PhookaEffectEventHandler {

	@SubscribeEvent	
	public static void onBlockHarvest(final PlaySoundAtEntityEvent event) {
		if(!event.isCanceled() && event.getEntity() instanceof PlayerEntity) {
			final PlayerEntity player = (PlayerEntity) event.getEntity();
			if(player.getActivePotionEffect(ModObjects.PHOOKA_CURSE_FOOTSTEPS) != null) {
				// replace footsteps sounds while potion effect is active
				if(event.getSound().getName().toString().contains(".step")) {
					if(player.getRNG().nextInt(3) != 0) {
						// cancel footsteps 2/3 of the time (to allow sound to play out)
						event.setCanceled(true);
					} else {
						// for the ones that aren't canceled, replace with ghast noises
						event.setSound(SoundEvents.ENTITY_GHAST_AMBIENT);
						event.setVolume(Math.min(event.getVolume() * 1.75F, 1.0F));
					}
				}
			}
		}
	}
}
