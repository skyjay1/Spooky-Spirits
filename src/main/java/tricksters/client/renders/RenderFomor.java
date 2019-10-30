package tricksters.client.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import tricksters.client.models.ModelFomor;
import tricksters.entity.FomorEntity;
import tricksters.init.Tricksters;

public class RenderFomor extends LivingRenderer<FomorEntity, ModelFomor> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Tricksters.MODID, "textures/entities/fomor.png");

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
