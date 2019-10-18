package spookyspirits.client.models;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import spookyspirits.entity.PhookaEntity;

public class ModelPhooka extends EntityModel<PhookaEntity> {
	public final RendererModel torso;
	public final RendererModel head;
	public final RendererModel lowerTorso;
	public final RendererModel backFur;
	public final RendererModel upperArm1;
	public final RendererModel upperArm2;
	public final RendererModel snout;
	public final RendererModel snout1;
	public final RendererModel rightHorn;
	public final RendererModel leftHorn;
	public final RendererModel ear1;
	public final RendererModel ear2;
	public final RendererModel hip1;
	public final RendererModel hip2;
	public final RendererModel tailBase;
	public final RendererModel leg1_1;
	public final RendererModel leg1_2;
	public final RendererModel foot1;
	public final RendererModel leg2_1;
	public final RendererModel leg2_2;
	public final RendererModel foot2;
	public final RendererModel tail1;
	public final RendererModel tail2;
	public final RendererModel tail2_1;
	public final RendererModel lowerArm1;
	public final RendererModel lowerArm2;

	public ModelPhooka() {
		textureWidth = 128;
		textureHeight = 128;
		leg1_1 = new RendererModel(this, 101, 0);
		leg1_1.setRotationPoint(3.4F, 0.6F, -0.4F);
		leg1_1.addBox(0.0F, 0.0F, -2.0F, 2, 8, 2, 0.0F);
		rotateDeg(leg1_1, 42, -1, -30); // last arg -15F
		foot1 = new RendererModel(this, 108, 50);
		foot1.setRotationPoint(-0.3F, 8.3F, -0.3F);
		foot1.addBox(0.0F, 0.0F, -2.0F, 2, 4, 3, 0.0F);
		rotateDeg(foot1, 0.0F, 0.0F, -48F);
		leg2_2 = new RendererModel(this, 111, 15);
		leg2_2.setRotationPoint(1.7F, 8.0F, 0.0F);
		leg2_2.addBox(0.0F, 0.0F, -2.0F, 2, 8, 2, 0.0F);
		rotateDeg(leg2_2, 0, 0, -99);
		leg1_2 = new RendererModel(this, 111, 0);
		leg1_2.setRotationPoint(1.7F, 8.0F, 0.0F);
		leg1_2.addBox(0.0F, 0.0F, -2.0F, 2, 8, 2, 0.0F);
		rotateDeg(leg1_2, 0, 0, -103);
		foot2 = new RendererModel(this, 94, 50);
		foot2.setRotationPoint(0.0F, 7.3F, -0.3F);
		foot2.addBox(0.0F, 0.0F, -2.0F, 2, 4, 3, 0.0F);
		rotateDeg(foot2, 0, 0, 1F);
		backFur = new RendererModel(this, 4, 31);
		backFur.setRotationPoint(2.0F, -4.0F, 0.0F);
		backFur.addBox(10.0F, 0.0F, -3.0F, 7, 4, 9, 0.0F);
		rotateDeg(backFur, 0, 0, 15);
		rightHorn = new RendererModel(this, 56, 0);
		rightHorn.setRotationPoint(-2.0F, -4.0F, 0.0F);
		rightHorn.addBox(-9.0F, -10.0F, 0.0F, 10, 11, 0, 0.0F);
		rotateDeg(rightHorn, 0, -21, -3);
		hip2 = new RendererModel(this, 83, 15);
		hip2.setRotationPoint(12.3F, 4.0F, -4.5F);
		hip2.addBox(0.0F, 0.0F, -2.0F, 5, 8, 3, 0.0F);
		rotateDeg(hip2, 41, 1, 133); // last arg -227
		snout1 = new RendererModel(this, 64, 41);
		snout1.setRotationPoint(4.3F, 0.0F, 1.0F);
		snout1.addBox(-5.0F, -3.0F, 0.0F, 4, 4, 2, 0.0F);
		rotateDeg(snout1, 0, 0, 20);
		snout = new RendererModel(this, 64, 51);
		snout.setRotationPoint(5.0F, 0.0F, 1.0F);
		snout.addBox(-5.0F, -3.0F, -1.0F, 4, 4, 4, 0.0F);
		upperArm1 = new RendererModel(this, 0, 60);
		upperArm1.setRotationPoint(15.0F, 0.0F, -2.9F);
		upperArm1.addBox(-1.0F, 0.0F, -10.0F, 2, 3, 11, 0.0F);
		rotateDeg(upperArm1, 18, 80, 0);
		lowerTorso = new RendererModel(this, 4, 19);
		lowerTorso.setRotationPoint(-8.8F, 5.8F, 0.0F);
		lowerTorso.addBox(7.0F, 1.0F, -1.0F, 10, 3, 5, 0.0F);
		rotateDeg(lowerTorso, 0, 0, -23);
		tail2_1 = new RendererModel(this, 80, 100);
		tail2_1.setRotationPoint(4.0F, -10.5F, 0.0F);
		tail2_1.addBox(0.0F, -6.0F, -1.0F, 2, 7, 2, 0.0F);
		rotateDeg(tail2_1, 0, 0, -158);
		lowerArm2 = new RendererModel(this, 30, 79);
		lowerArm2.setRotationPoint(-0.4F, 0.0F, -9.5F);
		lowerArm2.addBox(-1.0F, 0.0F, -11.0F, 2, 2, 10, 0.0F);
		rotateDeg(lowerArm2, 172, 266, -126);
		leg2_1 = new RendererModel(this, 101, 15);
		leg2_1.setRotationPoint(3.4F, 1.5F, 0.5F);
		leg2_1.addBox(0.0F, 0.0F, -2.0F, 2, 8, 2, 0.0F);
		rotateDeg(leg2_1, -37, 0, -22);
		ear1 = new RendererModel(this, 40, 0);
		ear1.setRotationPoint(-2.5F, -2.0F, 0.0F);
		ear1.addBox(0.0F, -1.0F, -3.0F, 1, 2, 4, 0.0F);
		rotateDeg(ear1, 0, 40, 24);
		head = new RendererModel(this, 40, 21);
		head.setRotationPoint(3.3F, 3.1F, -3.6F); // 17.3F, 5.1F, -0.6F
		head.addBox(0F, 0F, -5F, 6, 6, 6, 0.0F);
		rotateDeg(head, 0, 0, 90);
		lowerArm1 = new RendererModel(this, 0, 79);
		lowerArm1.setRotationPoint(0.6F, 3.0F, -9.5F);
		lowerArm1.addBox(-1.0F, 0.0F, -9.0F, 2, 2, 10, 0.0F);
		rotate(lowerArm1, 0.053930673886624786F, -1.514596724880679F, 2.211681228127214F);
		leftHorn = new RendererModel(this, 56, 0);
		leftHorn.setRotationPoint(-2.0F, -4.0F, 4.0F);
		leftHorn.addBox(-9.0F, -10.0F, 0.0F, 10, 11, 0, 0.0F);
		rotate(leftHorn, 0.0F, 0.3665191429188092F, -0.05235987755982988F);
		ear2 = new RendererModel(this, 40, 9);
		ear2.setRotationPoint(-2.5F, -1.0F, 5.0F);
		ear2.addBox(0.0F, -1.0F, -3.0F, 1, 2, 4, 0.0F);
		rotate(ear2, 0.0F, 2.251474735072685F, 0.41887902047863906F);
		tail2 = new RendererModel(this, 80, 100);
		tail2.setRotationPoint(1.5F, 1.7F, 0.0F);
		tail2.addBox(0.0F, -6.0F, -1.0F, 2, 7, 2, 0.0F);
		rotate(tail2, 0.0F, 0.0F, -2.768266726588206F);
		torso = new RendererModel(this, 4, 2);
		torso.setRotationPoint(1.0F, 0.0F, 0.0F);
		torso.addBox(7.0F, 0.0F, -2.0F, 10, 4, 7, 0.0F);
		rotateDeg(torso, 0, 0, -90);
		tailBase = new RendererModel(this, 100, 100);
		tailBase.setRotationPoint(6.5F, 2.0F, 1.5F);
		tailBase.addBox(0.0F, -6.0F, -1.0F, 2, 7, 2, 0.0F);
		rotateDeg(tailBase, 0, 0, 28);
		hip1 = new RendererModel(this, 83, 0);
		hip1.setRotationPoint(11.7F, 3.8F, 8.6F); // 11.7F, 3.8F, 8.6F
		hip1.addBox(0.0F, 0.0F, -2.0F, 5, 8, 3, 0.0F);
		rotateDeg(hip1, -41, 0, -180); // -41, 0, -227
		tail1 = new RendererModel(this, 80, 100);
		tail1.setRotationPoint(5.6F, -8.5F, 0.0F);
		tail1.addBox(0.0F, -6.0F, -1.0F, 2, 7, 2, 0.0F);
		rotate(tail1, 0.0F, 0.0F, -2.3528783646135554F);
		upperArm2 = new RendererModel(this, 30, 60);
		upperArm2.setRotationPoint(15.4F, 0.0F, 6.4F);
		upperArm2.addBox(-1.0F, 0.0F, -10.0F, 2, 3, 11, 0.0F);
		rotate(upperArm2, 0.3141592653589793F, 1.7540558982543013F, 0.0F);

		// torso + head
		torso.addChild(head);
		head.addChild(rightHorn);
		head.addChild(leftHorn);
		head.addChild(snout1);
		head.addChild(snout);
		head.addChild(ear1);
		head.addChild(ear2);

		// torso + upperArm1
		torso.addChild(upperArm1);
		upperArm1.addChild(lowerArm1);

		// torso + upperArm2
		torso.addChild(upperArm2);
		upperArm2.addChild(lowerArm2);

		// torso + lowerTorso
		torso.addChild(lowerTorso);
		lowerTorso.addChild(hip1);
		lowerTorso.addChild(hip2);
		lowerTorso.addChild(tailBase);
		// tailBase
		tailBase.addChild(tail1);
		tail1.addChild(tail2);
		tail2.addChild(tail2_1);
		// hip1
		hip1.addChild(leg1_1);
		leg1_1.addChild(leg1_2);
		leg1_2.addChild(foot1);
		// hip2 + leg
		hip2.addChild(leg2_1);
		leg2_1.addChild(leg2_2);
		leg2_2.addChild(foot2);
		
		// torso + backFur
		torso.addChild(backFur);
	}

	@Override
	public void render(final PhookaEntity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks,
			final float netHeadYaw, final float headPitch, final float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.rotatef(90F, 0F, 1F, 0F);
		this.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.translatef(0F, 2.0F * scale, 0F);
		torso.render(scale);
		
		
		GlStateManager.popMatrix();
	}
	
	@Override
	public void setRotationAngles(final PhookaEntity entity, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {

		// tail
		float tailAngleY = (float) Math.cos(entity.ticksExisted * 0.05F) * 0.1F;
		tailBase.rotateAngleY = tailAngleY;
		tail1.rotateAngleY = tailAngleY * 0.5F;
		tail2.rotateAngleY = tailAngleY * 0.25F;
		// head
		//this.head.rotateAngleZ = (float)Math.toRadians(90 + headPitch);
		this.head.rotateAngleY = (float)Math.toRadians(netHeadYaw);

		// DEBUG:
		//hip1.rotateAngleZ += 0.02F;
//		head.rotateAngleZ += 0.02F;
//		tailBase.rotateAngleX += 0.02F;
	}
	
	private static void rotateDeg(final RendererModel model, final float x, final float y, final float z) {
		rotate(model, (float)Math.toRadians(x), (float)Math.toRadians(y), (float)Math.toRadians(z));
	}

	private static void rotate(final RendererModel model, final float x, final float y, final float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}