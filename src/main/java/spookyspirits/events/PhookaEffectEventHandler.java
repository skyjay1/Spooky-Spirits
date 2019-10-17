package spookyspirits.events;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import spookyspirits.init.ModObjects;

@EventBusSubscriber
public class PhookaEffectEventHandler {

	@SubscribeEvent	
	public static void onBlockHarvest(final BlockEvent.HarvestDropsEvent event) {
		if(event.getHarvester() != null 
				&& event.getHarvester().getActivePotionEffect(ModObjects.PHOOKA_BLESSING_FORTUNE) != null) {
			// spawn different drops if they have this effect active
			//event.getDrops().clear();
			//event.getDrops().addAll(event.getState().harv)
		}
	}
}
