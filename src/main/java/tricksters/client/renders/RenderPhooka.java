package tricksters.client.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import tricksters.client.models.ModelPhooka;
import tricksters.entity.PhookaEntity;
import tricksters.init.Tricksters;

public class RenderPhooka extends LivingRenderer<PhookaEntity, ModelPhooka> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Tricksters.MODID, "textures/entities/phooka.png");

	public RenderPhooka(EntityRendererManager manager) {
		super(manager, new ModelPhooka(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(final PhookaEntity entityIn) {
		return TEXTURE;
	}
}
