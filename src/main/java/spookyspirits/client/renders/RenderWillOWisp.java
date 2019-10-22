package spookyspirits.client.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import spookyspirits.client.models.ModelWillOWisp;
import spookyspirits.entity.WillOWispEntity;
import spookyspirits.init.SpookySpirits;

public class RenderWillOWisp extends LivingRenderer<WillOWispEntity, ModelWillOWisp> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(SpookySpirits.MODID, "textures/entities/willowisp.png");

	public RenderWillOWisp(EntityRendererManager manager) {
		super(manager, new ModelWillOWisp(), 0.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(WillOWispEntity arg0) {
		return TEXTURE;
	}

	@Override
	protected boolean canRenderName(WillOWispEntity entity) {
		return false;
	}
}
