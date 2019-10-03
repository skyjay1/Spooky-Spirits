package spookyspirits.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import spookyspirits.entity.PossessedPumpkin;
import spookyspirits.init.SpookySpirits;

public class RenderPossessedPumpkin extends LivingRenderer<PossessedPumpkin, ModelPossessedPumpkin> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(SpookySpirits.MODID, "textures/entities/possessed_pumpkin.png");

	public RenderPossessedPumpkin(EntityRendererManager manager) {
		super(manager, new ModelPossessedPumpkin(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(PossessedPumpkin entity) {
		return TEXTURE;
	}

}
