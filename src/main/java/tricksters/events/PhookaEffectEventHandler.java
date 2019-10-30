package tricksters.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import tricksters.init.ModObjects;

@EventBusSubscriber
public class PhookaEffectEventHandler {

	@SubscribeEvent	
	public static void onPlaySound(final PlaySoundAtEntityEvent event) {
		if(!event.isCanceled() && event.getEntity() instanceof PlayerEntity) {
			final PlayerEntity player = (PlayerEntity) event.getEntity();
			if(player.getActivePotionEffect(ModObjects.PHOOKA_CURSE_FOOTSTEPS) != null) {
				// replace footsteps sounds while potion effect is active
				if(event.getSound().getName().toString().contains(".step")) {
					if(player.getRNG().nextFloat() < 0.4F) {
						// cancel footsteps some of the time (to allow sound to play out)
						event.setCanceled(true);
					} else {
						// for the ones that aren't canceled, replace with ghast noises
						// (sometimes ghast scream but usually ambient noise)
						event.setSound(player.getRNG().nextFloat() < 0.2F 
								? SoundEvents.ENTITY_GHAST_SCREAM 
								: SoundEvents.ENTITY_GHAST_AMBIENT);
						event.setVolume(Math.min(event.getVolume() * 2.0F, 1.0F));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlaceSpawns(final WorldEvent.PotentialSpawns event) {
		// TODO maybe we need to handle it here?
	}
}
