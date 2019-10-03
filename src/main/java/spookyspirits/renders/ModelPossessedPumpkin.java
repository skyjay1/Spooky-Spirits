package spookyspirits.renders;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import spookyspirits.entity.PossessedPumpkin;

public class ModelPossessedPumpkin extends EntityModel<PossessedPumpkin> {
	
	private final RendererModel legs;
	private final RendererModel block;

	public ModelPossessedPumpkin() {
		textureWidth = 64;
		textureHeight = 32;

		legs = new RendererModel(this);
		legs.setRotationPoint(0.0F, 24.0F, 0.0F);
		legs.cubeList.add(new ModelBox(legs, 10, 0, -7.0F, -4.0F, -6.0F, 1, 4, 1, 0.0F, false));
		legs.cubeList.add(new ModelBox(legs, 0, 8, -7.0F, -4.0F, 0.0F, 1, 4, 1, 0.0F, false));
		legs.cubeList.add(new ModelBox(legs, 7, 7, -7.0F, -4.0F, 5.0F, 1, 4, 1, 0.0F, false));
		legs.cubeList.add(new ModelBox(legs, 6, 0, 6.0F, -4.0F, -6.0F, 1, 4, 1, 0.0F, false));
		legs.cubeList.add(new ModelBox(legs, 3, 4, 6.0F, -4.0F, 0.0F, 1, 4, 1, 0.0F, false));
		legs.cubeList.add(new ModelBox(legs, 0, 0, 6.0F, -4.0F, 5.0F, 1, 4, 1, 0.0F, false));

		block = new RendererModel(this);
		block.setRotationPoint(0.0F, 24.0F, 0.0F);
		block.cubeList.add(new ModelBox(block, 0, 0, -8.0F, -16.0F, -8.0F, 16, 16, 16, 0.0F, false));
	}

	@Override
	public void render(final PossessedPumpkin entityIn, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
		
		GlStateManager.pushMatrix();
		float standing = entityIn.getStandingTicksRatio() * 4.0F;
		GlStateManager.translatef(0, -standing * scale, 0);
		block.render(scale);
		GlStateManager.popMatrix();
		
		legs.render(scale);

	}
	public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
