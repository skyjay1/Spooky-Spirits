package spookyspirits.client.models;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import spookyspirits.entity.PhookaEntity;

public class ModelPhooka extends EntityModel<PhookaEntity> {
	private final RendererModel body;
	private final RendererModel torso;
	private final RendererModel upperTorso;
	private final RendererModel lowerTorso;
	private final RendererModel hip1;
	private final RendererModel legUpper1;
	private final RendererModel legLower1;
	private final RendererModel foot1;
	private final RendererModel hip2;
	private final RendererModel legUpper2;
	private final RendererModel legLower2;
	private final RendererModel foot2;
	private final RendererModel head;
	private final RendererModel horn1;
	private final RendererModel horn2;
	private final RendererModel snout1;
	private final RendererModel ear1;
	private final RendererModel ear2;
	private final RendererModel tailBase;
	private final RendererModel tail1;
	private final RendererModel tail2;
	private final RendererModel tail3;
	private final RendererModel arm1;
	private final RendererModel armLower1;
	private final RendererModel arm2;
	private final RendererModel armLower2;

	public ModelPhooka() {
		textureWidth = 128;
		textureHeight = 128;

		body = new RendererModel(this);
		body.setRotationPoint(0.0F, 10.0F, 0.0F);

		torso = new RendererModel(this);
		torso.setRotationPoint(0.0F, -9.0F, 5.0F);
		body.addChild(torso);

		upperTorso = new RendererModel(this);
		upperTorso.setRotationPoint(0.0F, 2.0F, -1.0F);
		rotate(upperTorso, 0.1745F, 0.0F, 0.0F);
		torso.addChild(upperTorso);
		upperTorso.cubeList.add(new ModelBox(upperTorso, 29, 32, -3.5F, -2.5F, -3.0F, 7, 4, 4, 0.0F, false));
		upperTorso.cubeList.add(new ModelBox(upperTorso, 0, 29, -5.0F, -9.5F, -3.0F, 10, 7, 4, 0.0F, false));

		lowerTorso = new RendererModel(this);
		lowerTorso.setRotationPoint(0.0F, 0.0F, -2.0F);
		rotate(lowerTorso, -0.5236F, 0.0F, 0.0F);
		upperTorso.addChild(lowerTorso);
		lowerTorso.cubeList.add(new ModelBox(lowerTorso, 52, 30, -2.5F, 0.0F, 0.0F, 5, 7, 3, 0.0F, false));

		hip1 = new RendererModel(this);
		hip1.setRotationPoint(-2.5F, 1.0F, 3.0F);
		body.addChild(hip1);
		hip1.cubeList.add(new ModelBox(hip1, 0, 86, -3.0F, -4.0F, -7.0F, 3, 5, 8, 0.0F, false));

		legUpper1 = new RendererModel(this);
		legUpper1.setRotationPoint(-1.5F, 1.0F, -7.0F);
		rotate(legUpper1, 0.5236F, 0.0F, 0.0F);
		hip1.addChild(legUpper1);
		legUpper1.cubeList.add(new ModelBox(legUpper1, 25, 86, -1.0F, 0.0F, 0.0F, 2, 8, 2, 0.0F, false));

		legLower1 = new RendererModel(this);
		legLower1.setRotationPoint(0.0F, 8.0F, 2.0F);
		legUpper1.addChild(legLower1);
		legLower1.cubeList.add(new ModelBox(legLower1, 35, 86, -1.0F, 0.0F, -8.0F, 2, 2, 8, 0.0F, false));

		foot1 = new RendererModel(this);
		foot1.setRotationPoint(0.0F, 0.0F, -8.0F);
		rotate(foot1, 1.0472F, 0.0F, 0.0F);
		legLower1.addChild(foot1);
		foot1.cubeList.add(new ModelBox(foot1, 57, 86, -1.5F, -3.0F, -2.0F, 3, 4, 2, 0.0F, false));

		hip2 = new RendererModel(this);
		hip2.setRotationPoint(2.5F, 1.0F, 3.0F);
		body.addChild(hip2);
		hip2.cubeList.add(new ModelBox(hip2, 0, 70, 0.0F, -4.0F, -7.0F, 3, 5, 8, 0.0F, false));

		legUpper2 = new RendererModel(this);
		legUpper2.setRotationPoint(1.5F, 1.0F, -7.0F);
		rotate(legUpper2, 0.5236F, 0F, 0.0F);
		hip2.addChild(legUpper2);
		legUpper2.cubeList.add(new ModelBox(legUpper2, 25, 70, -1.0F, 0.0F, 0.0F, 2, 8, 2, 0.0F, false));

		legLower2 = new RendererModel(this);
		legLower2.setRotationPoint(0.0F, 8.0F, 2.0F);
		legUpper2.addChild(legLower2);
		legLower2.cubeList.add(new ModelBox(legLower2, 35, 70, -1.0F, 0.0F, -8.0F, 2, 2, 8, 0.0F, false));

		foot2 = new RendererModel(this);
		foot2.setRotationPoint(0.0F, 0.0F, -8.0F);
		rotate(foot2, 1.0472F, 0.0F, 0.0F);
		legLower2.addChild(foot2);
		foot2.cubeList.add(new ModelBox(foot2, 57, 70, -1.5F, -3.0F, -2.0F, 3, 4, 2, 0.0F, false));

		head = new RendererModel(this);
		head.setRotationPoint(0.0F, -6.5F, 2.0F);
		head.cubeList.add(new ModelBox(head, 0, 14, -3.0F, -5.5F, -5.0F, 6, 6, 6, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 25, 14, -2.0F, -3.5F, -9.0F, 4, 4, 4, 0.0F, false));

		horn1 = new RendererModel(this);
		horn1.setRotationPoint(-2.0F, -5.5F, -2.0F);
		rotate(horn1, 1.5708F, 1.3963F, 0.0F);
		head.addChild(horn1);
		horn1.cubeList.add(new ModelBox(horn1, 0, 0, -8.0F, 0.0F, -1.0F, 10, 0, 11, 0.0F, false));

		horn2 = new RendererModel(this);
		horn2.setRotationPoint(2.0F, -5.5F, -2.0F);
		rotate(horn2, 1.5708F, 1.7453F, 0.0F);
		head.addChild(horn2);
		horn2.cubeList.add(new ModelBox(horn2, 44, 0, -8.0F, 0.0F, -1.0F, 10, 0, 11, 0.0F, false));

		snout1 = new RendererModel(this);
		snout1.setRotationPoint(0.0F, -5.5F, -5.0F);
		rotate(snout1, 0.4363F, 0.0F, 0.0F);
		head.addChild(snout1);
		snout1.cubeList.add(new ModelBox(snout1, 42, 14, -2.0F, 0.25F, -4.0F, 4, 2, 4, 0.0F, false));

		ear1 = new RendererModel(this);
		ear1.setRotationPoint(-3.0F, -3.5F, -1.0F);
		rotate(ear1, -1.0472F, -0.2618F, -0.4363F);
		head.addChild(ear1);
		ear1.cubeList.add(new ModelBox(ear1, 42, 21, -1.0F, -3.0F, -1.0F, 1, 4, 2, 0.0F, false));

		ear2 = new RendererModel(this);
		ear2.setRotationPoint(3.0F, -3.5F, -1.0F);
		rotate(ear2, -1.0472F, 0.2618F, 0.4363F);
		head.addChild(ear2);
		ear2.cubeList.add(new ModelBox(ear2, 49, 21, 0.0F, -3.0F, -1.0F, 1, 4, 2, 0.0F, false));

		tailBase = new RendererModel(this);
		tailBase.setRotationPoint(0.0F, 10.0F, 3.0F);
		rotate(tailBase, 0.3491F, 0.0F, 0.0F);
		tailBase.cubeList.add(new ModelBox(tailBase, 90, 0, -1.0F, -2.0F, 0.0F, 2, 2, 7, 0.0F, false));

		tail1 = new RendererModel(this);
		tail1.setRotationPoint(0.0F, 0.0F, 7.0F);
		rotate(tail1, 0.4363F, 0.0F, 0.0F);
		tailBase.addChild(tail1);
		tail1.cubeList.add(new ModelBox(tail1, 90, 11, -1.0F, -2.0F, 0.0F, 2, 2, 7, 0.0F, false));

		tail2 = new RendererModel(this);
		tail2.setRotationPoint(0.0F, 0.0F, 7.0F);
		rotate(tail2, 0.6109F, 0.0F, 0.0F);
		tail1.addChild(tail2);
		tail2.cubeList.add(new ModelBox(tail2, 90, 22, -1.0F, -2.0F, 0.0F, 2, 2, 7, 0.0F, false));

		tail3 = new RendererModel(this);
		tail3.setRotationPoint(0.0F, 0.0F, 7.0F);
		rotate(tail3, 0.3491F, 0.0F, 0.0F);
		tail2.addChild(tail3);
		tail3.cubeList.add(new ModelBox(tail3, 90, 33, -1.0F, -2.0F, 0.0F, 2, 2, 7, 0.0F, false));

		arm1 = new RendererModel(this);
		arm1.setRotationPoint(-5.0F, -5.0F, 2.0F);
		arm1.cubeList.add(new ModelBox(arm1, 0, 44, -2.0F, -1.0F, -2.0F, 2, 9, 3, 0.0F, false));

		armLower1 = new RendererModel(this);
		armLower1.setRotationPoint(-1.0F, 8.0F, 0.0F);
		rotate(armLower1, 0.0F, -0.6109F, 0.0F);
		arm1.addChild(armLower1);
		armLower1.cubeList.add(new ModelBox(armLower1, 11, 44, -1.0F, 0.0F, -9.0F, 2, 2, 10, 0.0F, false));

		arm2 = new RendererModel(this);
		arm2.setRotationPoint(5.0F, -5.0F, 2.0F);
		arm2.cubeList.add(new ModelBox(arm2, 36, 44, 0.0F, -1.0F, -2.0F, 2, 9, 3, 0.0F, false));

		armLower2 = new RendererModel(this);
		armLower2.setRotationPoint(1.0F, 8.0F, 0.0F);
		rotate(armLower2, 0.0F, 0.6109F, 0.0F);
		arm2.addChild(armLower2);
		armLower2.cubeList.add(new ModelBox(armLower2, 47, 44, -1.0F, 0.0F, -9.0F, 2, 2, 10, 0.0F, false));
	}

	@Override
	public void render(final PhookaEntity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks,
			final float netHeadYaw, final float headPitch, final float scale) {
		GlStateManager.pushMatrix();
		this.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		// if sitting, apply transformations
		if(entity.isSitting()) {
			GlStateManager.translatef(0F, 8F * scale, -2F * scale);
		} else {
			GlStateManager.translatef(0F, -3.25F * scale, 0F * scale);
		}
		
		// enable transparency
		final float fade = MathHelper.clamp(entity.getFadeFactor(), 0.0F, 0.6F);
		GlStateManager.enableBlend();
		GlStateManager.enableNormalize();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.color4f(0.0F, 0.0F, 0.0F, fade);
		
		// actually render the parts
		body.render(scale);
		head.render(scale);
		tailBase.render(scale);
		arm1.render(scale);
		arm2.render(scale);
		
		GlStateManager.popMatrix();
	}
	
	@Override
	public void setRotationAngles(final PhookaEntity entity, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
		// tail
		float tailAngleZ = (float) Math.cos(entity.ticksExisted * 0.1F) * 0.09F;
		tailBase.rotateAngleZ = tailAngleZ;
		tail1.rotateAngleZ = tailAngleZ * 0.5F;
		tail2.rotateAngleZ = tailAngleZ * 0.25F;
		// head
		this.head.rotateAngleY = (float)Math.toRadians(netHeadYaw);
		this.head.rotateAngleX = (float)Math.toRadians(headPitch);
		// legs
		if(entity.isSitting()) {
			sit();
		} else {
			// STANDING / WALKING
			final float speed = 1.15F;
			final float angle = 1.6F;
			final float rad30 = (float)Math.toRadians(30D);
			final float rad40 = (float)Math.toRadians(40D);
			// use sin and cosine to determine limb angles
			final float limbAngle1 = MathHelper.cos(limbSwing * speed) * angle * limbSwingAmount;
			final float limbAngle2 = MathHelper.cos(limbSwing * speed + (float)Math.PI) * angle * limbSwingAmount;
			// hips
			hip1.rotateAngleX = limbAngle1 + rad40;
			hip2.rotateAngleX = limbAngle2 + rad40;
			// feet
			foot1.rotateAngleX = rad30 * 3.0F - hip1.rotateAngleX;
			foot2.rotateAngleX = rad30 * 3.0F - hip2.rotateAngleX;
			// arms
			arm1.rotateAngleX = limbAngle2 * 0.65F;
			arm2.rotateAngleX = limbAngle1 * 0.65F;
			// hips and legs (constants used when standing)
			//if(Math.abs(hip1.rotateAngleY) > 0.001F) {
				stand();
			//}
		}
	}
	
	/**
	 * Sets angles for hips, legs, feet, and arms to adapt a "sitting" pose
	 **/
	private void sit() {
		final float rad15 = (float)Math.toRadians(15D);
		final float rad20 = (float)Math.toRadians(20D);
		// all of these are constants
		hip1.rotateAngleX = hip2.rotateAngleX = -rad20;
		hip1.rotateAngleY = rad20;
		hip2.rotateAngleY = -rad20;
		legUpper1.rotateAngleY = -rad20;
		legUpper2.rotateAngleY = rad20;
		legUpper1.rotateAngleX = legUpper2.rotateAngleX = (3 * rad15) + rad20;
		legLower1.rotateAngleX = legLower2.rotateAngleX = -(3 * rad15);
		foot1.rotateAngleX = foot2.rotateAngleX = 3 * rad20;
		arm1.rotateAngleX = arm2.rotateAngleX = 0;
	}
	
	/**
	 * Undo some of the angles set in {@link #sit()}
	 **/
	private void stand() {
		hip1.rotateAngleY = hip2.rotateAngleY = 0.0F;
		legUpper1.rotateAngleX = legUpper2.rotateAngleX = 0;
		legUpper1.rotateAngleY = legUpper2.rotateAngleY = 0;
		legLower1.rotateAngleX = legLower2.rotateAngleX = 0;
	}
	
	private static void rotate(final RendererModel model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}