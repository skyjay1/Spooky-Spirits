package spookyspirits.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import spookyspirits.entity.WillOWisp;
import spookyspirits.init.SpookySpirits;

public class RenderWillOWisp extends LivingRenderer<WillOWisp, ModelWillOWisp> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(SpookySpirits.MODID, "textures/entities/willowisp.png");

	public RenderWillOWisp(EntityRendererManager manager) {
		super(manager, new ModelWillOWisp(), 0.25F);
	}

	@Override
	protected ResourceLocation getEntityTexture(WillOWisp arg0) {
		return TEXTURE;
	}

}
