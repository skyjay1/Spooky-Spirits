package spookyspirits.client.models;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import spookyspirits.entity.WillOWispEntity;

public class ModelWillOWisp extends EntityModel<WillOWispEntity> {
	
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
	public void render(final WillOWispEntity entityIn, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
		GlStateManager.pushMatrix();
		
		this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		// fade in and out
		float fading = getFadeFactor(entityIn.ticksExisted + entityIn.getEntityId() * 10, 0.065F, 1.1F);
		GlStateManager.enableBlend();
		GlStateManager.enableNormalize();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, fading);
		
		cube.render(scale);
		
		GlStateManager.popMatrix();
	}
	
	@Override
	public void setRotationAngles(final WillOWispEntity entity, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
		float bobbing = (float)Math.sin((entity.ticksExisted + entity.getEntityId()) * 0.09D) * 0.28F;
		GlStateManager.translatef(0F, (6.0F + bobbing) * scaleFactor, 0F);
	}
	
	/**
	 * @param ticks the number of ticks elapsed since beginning
	 * @param fadeSpeed affects the amount of change in alpha per tick
	 * @param amplitude affects the amount of time spent fully transparent or fully opaque
	 * @return a number between 0.0F and 1.0F, where 0.0F is fully transparent
	 **/
	private static float getFadeFactor(final float ticks, final float fadeSpeed, final float amplitude) {
		// adding this means more time will be spent at higher values
		float upShift = amplitude * 0.6F;
		// f: a number between (-amplitude) and (amplitude), with (upShift) added
		float fade = (float)Math.sin(ticks * fadeSpeed) * amplitude + upShift;
		// return a single float between 0 and 1.0 (time spent outside of these bounds is clamped)
		return MathHelper.clamp(fade, 0.0F, 1.0F);
	}
}
