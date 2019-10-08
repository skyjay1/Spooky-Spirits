package spookyspirits.renders;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import spookyspirits.entity.FlyingSkull;

public class ModelFlyingSkull extends EntityModel<FlyingSkull> {
	
	private final RendererModel head;
//	private final RendererModel flame1;
//	private final RendererModel flame2;

	public ModelFlyingSkull() {
		
		head = new RendererModel(this);
		head.setTextureSize(128, 64);
		head.setRotationPoint(0.0F, 24.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -9.0F, -4.0F, 8, 8, 8, 0.0F, false));

//		flame1 = new RendererModel(this);
//		flame1.setTextureSize(16, 128);
//		flame1.setRotationPoint(0.0F, 24.0F, 0.0F);
//		setAngles(flame1, 0.0F, -0.7854F, 0.0F);
//		flame1.cubeList.add(new ModelBox(flame1, 0, -12, 0.0F, -16.0F, -6.0F, 0, 16, 12, 0.0F, true));
//
//		flame2 = new RendererModel(this);
//		flame2.setTextureSize(16, 128);
//		flame2.setRotationPoint(0.0F, 24.0F, 0.0F);
//		setAngles(flame2, 0.0F, 0.7854F, 0.0F);
//		flame2.cubeList.add(new ModelBox(flame2, 0, -12, 0.0F, -16.0F, -6.0F, 0, 16, 12, 0.0F, true));
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(final FlyingSkull entityIn, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
		GlStateManager.pushMatrix();
		this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.disableCull();
		head.render(scale);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(final FlyingSkull entity, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
//		  this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
//	      this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
	}
	
//	public void renderFlames(final FlyingSkull entityIn, final float limbSwing, final float limbSwingAmount,
//			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
//		//final float scalef = 1.4F;
//		//GlStateManager.pushMatrix();
//		GlStateManager.enableNormalize();
//		GlStateManager.enableBlend();
//		GlStateManager.blendFunc(770, 771);
//		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.7F);
//		//GlStateManager.scalef(scalef, 1.0F, scalef);
//		
//		flame1.render(scale);
//		flame2.render(scale);
//
//		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//		GlStateManager.disableBlend();
//		GlStateManager.disableNormalize();
//		//GlStateManager.popMatrix();
//	}
//	
//	public void renderSkull(final FlyingSkull entityIn, final float limbSwing, final float limbSwingAmount,
//			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
//		//GlStateManager.pushMatrix();
//		//GlStateManager.enableBlend();
//		
//		//GlStateManager.disableBlend();
//		//GlStateManager.popMatrix();
//	}
//	
//	public void setFlameTextureY(final int offsetY) {
//		flame1.setTextureOffset(0, offsetY - 12);
//		flame2.setTextureOffset(0, offsetY - 12);
//	}
	
//	private static void setAngles(final RendererModel box, final float x, final float y, final float z) {
//		box.rotateAngleX = x;
//		box.rotateAngleY = y;
//		box.rotateAngleZ = z;
//	}
}
