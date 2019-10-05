package spookyspirits.renders;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import spookyspirits.entity.WillOWisp;

public class ModelWillOWisp extends EntityModel<WillOWisp> {
	
	private final RendererModel cube;

	public ModelWillOWisp() {
		textureWidth = 32;
		textureHeight = 16;

		cube = new RendererModel(this);
		cube.rotateAngleX = 0.0F;
		cube.rotateAngleY = 24.0F;
		cube.rotateAngleZ = 0.0F;
		cube.cubeList.add(new ModelBox(cube, 0, 0, -3.0F, 6F, -3.0F, 6, 6, 6, 0.0F, false));
	}

	@Override
	public void render(final WillOWisp entityIn, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
		GlStateManager.pushMatrix();
		
		this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		// fade in and out
		float fading = WillOWisp.getFadeFactor(entityIn.ticksExisted + entityIn.getEntityId(), 0.08F, 1.55F);
		GlStateManager.enableBlend();
		GlStateManager.enableNormalize();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, fading);
		
		cube.render(scale);
		
		GlStateManager.popMatrix();
	}
	
	@Override
	public void setRotationAngles(final WillOWisp entity, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
		float bobbing = (float)Math.sin((entity.ticksExisted + entity.getEntityId()) * 0.09D) * 0.28F;
		GlStateManager.translatef(0F, bobbing * scaleFactor, 0F);
	}
}
