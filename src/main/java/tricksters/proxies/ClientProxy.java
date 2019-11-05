package tricksters.proxies;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import tricksters.client.renders.*;
import tricksters.entity.*;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerEntityRenders() {
		// register renders
		RenderingRegistry.registerEntityRenderingHandler(WispEntity.class, RenderWisp::new);
		RenderingRegistry.registerEntityRenderingHandler(WillOWispEntity.class, RenderWillOWisp::new);
		RenderingRegistry.registerEntityRenderingHandler(PossessedPumpkinEntity.class, RenderPossessedPumpkin::new);
		RenderingRegistry.registerEntityRenderingHandler(PhookaEntity.class, RenderPhooka::new);
		RenderingRegistry.registerEntityRenderingHandler(FomorEntity.class, RenderFomor::new);
	}
}
