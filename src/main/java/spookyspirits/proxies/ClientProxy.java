package spookyspirits.proxies;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import spookyspirits.client.renders.*;
import spookyspirits.entity.*;

public class ClientProxy extends CommonProxy {
	
	
	@Override
	public void registerEntityRenders() {
		// register renders
		RenderingRegistry.registerEntityRenderingHandler(FlyingSkull.class, RenderFlyingSkull::new);
		RenderingRegistry.registerEntityRenderingHandler(WillOWispEntity.class, RenderWillOWisp::new);
		RenderingRegistry.registerEntityRenderingHandler(PossessedPumpkinEntity.class, RenderPossessedPumpkin::new);
		RenderingRegistry.registerEntityRenderingHandler(PhookaEntity.class, RenderPhooka::new);
		RenderingRegistry.registerEntityRenderingHandler(FomorEntity.class, RenderFomor::new);
	}
}
