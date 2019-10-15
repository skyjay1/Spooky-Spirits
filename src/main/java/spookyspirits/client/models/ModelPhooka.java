package spookyspirits.client.models;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.BipedModel;
import spookyspirits.entity.PhookaEntity;

public class ModelPhooka extends BipedModel<PhookaEntity> {
	
	
	public ModelPhooka() {
		super();
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(final PhookaEntity entityIn, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
		GlStateManager.pushMatrix();
		
		if(entityIn.isDespawning()) {
			GlStateManager.enableBlend();
			GlStateManager.enableNormalize();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, entityIn.getFadeFactor());
		}
		
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.popMatrix();

	}

	@Override
	public void setRotationAngles(final PhookaEntity entity, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
//		  this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
//	      this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
	}
}
