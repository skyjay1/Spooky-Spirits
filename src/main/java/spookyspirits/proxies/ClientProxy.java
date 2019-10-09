package spookyspirits.proxies;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import spookyspirits.entity.FlyingSkull;
import spookyspirits.entity.PhookaEntity;
import spookyspirits.entity.PossessedPumpkinEntity;
import spookyspirits.entity.WillOWispEntity;
import spookyspirits.renders.RenderFlyingSkull;
import spookyspirits.renders.RenderPhooka;
import spookyspirits.renders.RenderPossessedPumpkin;
import spookyspirits.renders.RenderWillOWisp;

public class ClientProxy extends CommonProxy {
	
	
	@Override
	public void registerEntityRenders() {
		// register renders
		RenderingRegistry.registerEntityRenderingHandler(FlyingSkull.class, RenderFlyingSkull::new);
		RenderingRegistry.registerEntityRenderingHandler(WillOWispEntity.class, RenderWillOWisp::new);
		RenderingRegistry.registerEntityRenderingHandler(PossessedPumpkinEntity.class, RenderPossessedPumpkin::new);
		RenderingRegistry.registerEntityRenderingHandler(PhookaEntity.class, RenderPhooka::new);
	}
}
