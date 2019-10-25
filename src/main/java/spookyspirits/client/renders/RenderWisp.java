package spookyspirits.client.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import spookyspirits.client.models.ModelWisp;
import spookyspirits.entity.WispEntity;
import spookyspirits.init.SpookySpirits;

public class RenderWisp extends LivingRenderer<WispEntity, ModelWisp> {
	
	private static final ResourceLocation VARIANT_BLUE = new ResourceLocation(SpookySpirits.MODID, "textures/entities/willowisp/blue.png");
	private static final ResourceLocation VARIANT_GREEN = new ResourceLocation(SpookySpirits.MODID, "textures/entities/willowisp/green.png");
	private static final ResourceLocation VARIANT_YELLOW = new ResourceLocation(SpookySpirits.MODID, "textures/entities/willowisp/yellow.png");

	public RenderWisp(EntityRendererManager manager) {
		super(manager, new ModelWisp(), 0.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(final WispEntity entity) {
		switch(entity.getVariant()) {
		case 2: return VARIANT_YELLOW;
		case 1: return VARIANT_GREEN;
		case 0: default: return VARIANT_BLUE;
		}
	}

	@Override
	protected boolean canRenderName(final WispEntity entity) {
		return false;
	}
}
