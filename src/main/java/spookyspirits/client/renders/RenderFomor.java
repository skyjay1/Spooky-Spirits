package spookyspirits.client.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import spookyspirits.client.models.ModelFomor;
import spookyspirits.entity.FomorEntity;
import spookyspirits.init.SpookySpirits;

public class RenderFomor extends LivingRenderer<FomorEntity, ModelFomor> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(SpookySpirits.MODID, "textures/entities/fomor.png");

	public RenderFomor(EntityRendererManager manager) {
		super(manager, new ModelFomor(), 0.45F);
	}

	@Override
	protected ResourceLocation getEntityTexture(final FomorEntity entityIn) {
		return TEXTURE;
	}
	
	@Override
	protected boolean canRenderName(FomorEntity entity) {
		return false;
	}
}
