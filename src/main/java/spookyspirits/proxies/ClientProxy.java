package spookyspirits.proxies;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import spookyspirits.entity.FlyingSkull;
import spookyspirits.entity.PossessedPumpkin;
import spookyspirits.entity.WillOWisp;
import spookyspirits.renders.RenderFlyingSkull;
import spookyspirits.renders.RenderPossessedPumpkin;
import spookyspirits.renders.RenderWillOWisp;

public class ClientProxy extends CommonProxy {
	
	
	@Override
	public void registerEntityRenders() {
		// register renders
		RenderingRegistry.registerEntityRenderingHandler(FlyingSkull.class, RenderFlyingSkull::new);
		RenderingRegistry.registerEntityRenderingHandler(WillOWisp.class, RenderWillOWisp::new);
		RenderingRegistry.registerEntityRenderingHandler(PossessedPumpkin.class, RenderPossessedPumpkin::new);
	}
}
