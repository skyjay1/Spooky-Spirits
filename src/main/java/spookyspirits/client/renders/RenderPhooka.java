package spookyspirits.client.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import spookyspirits.client.models.ModelPhooka;
import spookyspirits.entity.PhookaEntity;

public class RenderPhooka extends LivingRenderer<PhookaEntity, ModelPhooka> {

	private static final ResourceLocation SKULL_SKELETON = new ResourceLocation("minecraft:textures/entity/skeleton/skeleton.png");

	public RenderPhooka(EntityRendererManager manager) {
		super(manager, new ModelPhooka(), 0.35F);
	}

	@Override
	protected ResourceLocation getEntityTexture(final PhookaEntity entityIn) {
		return SKULL_SKELETON;
	}
}
