package spookyspirits.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import spookyspirits.entity.PossessedPumpkinEntity;
import spookyspirits.entity.WillOWispEntity;
import spookyspirits.init.SpookySpirits;

public class RenderPossessedPumpkin extends LivingRenderer<PossessedPumpkinEntity, ModelPossessedPumpkin> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(SpookySpirits.MODID, "textures/entities/possessed_pumpkin.png");

	public RenderPossessedPumpkin(EntityRendererManager manager) {
		super(manager, new ModelPossessedPumpkin(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(PossessedPumpkinEntity entity) {
		return TEXTURE;
	}
	
	@Override
	protected boolean canRenderName(PossessedPumpkinEntity entity) {
		return false;
	}

}
