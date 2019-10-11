package spookyspirits.renders;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import spookyspirits.entity.PossessedPumpkinEntity;

public class ModelPossessedPumpkin extends EntityModel<PossessedPumpkinEntity> {
	
	private final RendererModel block;
	private final RendererModel leg1;
	private final RendererModel leg2;
	private final RendererModel leg3;
	private final RendererModel leg4;
	private final RendererModel leg5;
	private final RendererModel leg6;

	public ModelPossessedPumpkin() {
		textureWidth = 64;
		textureHeight = 32;		
		
		leg1 = new RendererModel(this);
		leg1.setRotationPoint(-6.5F, 20.0F, -5.5F);
		leg1.cubeList.add(new ModelBox(leg1, 10, 0, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.0F, false));
		
		leg2 = new RendererModel(this);
		leg2.setRotationPoint(-6.5F, 20.0F, 0.5F);
		leg2.cubeList.add(new ModelBox(leg2, 0, 8, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.0F, false));
		
		leg3 = new RendererModel(this);
		leg3.setRotationPoint(-6.5F, 20.0F, 5.5F);
		leg3.cubeList.add(new ModelBox(leg3, 7, 7, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.0F, false));
		
		leg4 = new RendererModel(this);
		leg4.setRotationPoint(6.5F, 20.0F, -5.5F);
		leg4.cubeList.add(new ModelBox(leg4, 6, 0, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.0F, false));
		
		leg5 = new RendererModel(this);
		leg5.setRotationPoint(6.5F, 20.0F, 0.5F);
		leg5.cubeList.add(new ModelBox(leg5, 3, 4, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.0F, false));
		
		leg6 = new RendererModel(this);
		leg6.setRotationPoint(6.5F, 20.0F, 5.5F);
		leg6.cubeList.add(new ModelBox(leg6, 0, 0, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.0F, false));

		block = new RendererModel(this);
		block.setRotationPoint(0.0F, 24.0F, 0.0F);
		block.cubeList.add(new ModelBox(block, 0, 0, -8.0F, -16.0F, -8.0F, 16, 16, 16, 0.0F, false));
	}

	@Override
	public void render(final PossessedPumpkinEntity entityIn, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
		setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		leg1.render(scale);
		leg2.render(scale);
		leg3.render(scale);
		leg4.render(scale);
		leg5.render(scale);
		leg6.render(scale);
		
		// render head (pumpkin block)
		float standing = entityIn.getStandingTicksRatio() * 4.0F;
		GlStateManager.pushMatrix();
		GlStateManager.translatef(0, -standing * scale, 0); // scale = 0.0625F
		block.render(scale);
		GlStateManager.popMatrix();
	}
	
	@Override
	public void setRotationAngles(final PossessedPumpkinEntity entity, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
		super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		final float speed = 1.35F;
		final float angle = 2.0F;
		// leg angle 1
		final float legAngle1 = MathHelper.cos(limbSwing * speed) * angle * limbSwingAmount;
		// leg angle 2
		final float legAngle2 = MathHelper.cos(limbSwing * speed + (float)Math.PI) * angle * limbSwingAmount;
		// set rotations
		leg1.rotateAngleX = leg3.rotateAngleX = leg5.rotateAngleX = legAngle1;
		leg2.rotateAngleX = leg4.rotateAngleX = leg6.rotateAngleX = legAngle2;
	}
}
