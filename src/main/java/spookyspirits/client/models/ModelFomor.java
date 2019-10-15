package spookyspirits.client.models;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import spookyspirits.entity.FomorEntity;

public class ModelFomor extends EntityModel<FomorEntity> {

	private final RendererModel body;
	private final RendererModel arm1;
	private final RendererModel arm2;
	private final RendererModel leg;
	private final RendererModel legUpper;
	private final RendererModel legLower;
	private final RendererModel horn1a;
	private final RendererModel horn1b;
	private final RendererModel horn2a;
	private final RendererModel horn2b;

	private static final float legUpperAngleX = -0.1745F;
	private static final float legLowerAngleX = 0.5236F;
	private static final float armAngleZ = 0.6109F;

	public ModelFomor() {
		textureWidth = 32;
		textureHeight = 32;

		body = new RendererModel(this);
		body.setRotationPoint(0.0F, 15.5F, 0.0F);
		body.cubeList.add(new ModelBox(body, 0, 0, -3.0F, -2.5F, -2.0F, 6, 6, 4, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 10, 10, -1.5F, -1.5F, -3.0F, 3, 3, 1, 0.0F, false));

		arm1 = new RendererModel(this);
		arm1.setRotationPoint(-3.0F, 0.5F, 0.5F);
		setAngles(arm1, 0.0F, 0.0F, -armAngleZ);
		body.addChild(arm1);
		arm1.cubeList.add(new ModelBox(arm1, 0, 14, -4.0F, 0.0F, -0.5F, 4, 1, 1, 0.0F, false));

		arm2 = new RendererModel(this);
		arm2.setRotationPoint(3.0F, 0.5F, 0.5F);
		setAngles(arm2, 0.0F, 0.0F, armAngleZ);
		body.addChild(arm2);
		arm2.cubeList.add(new ModelBox(arm2, 18, 10, 0.0F, 0.0F, -0.5F, 4, 1, 1, 0.0F, false));

		leg = new RendererModel(this);
		leg.setRotationPoint(0.0F, 12.0F, 0.0F);

		legUpper = new RendererModel(this);
		legUpper.setRotationPoint(0.0F, 7.0F, 0.0F);
		setAngles(legUpper, legUpperAngleX, 0.0F, 0.0F);
		leg.addChild(legUpper);
		legUpper.cubeList.add(new ModelBox(legUpper, 10, 14, -1.0F, 0.0F, -1.0F, 2, 2, 2, 0.0F, false));

		legLower = new RendererModel(this);
		legLower.setRotationPoint(0.0F, 1.5F, 0.0F);
		setAngles(legLower, legLowerAngleX, 0.0F, 0.0F);
		legUpper.addChild(legLower);
		legLower.cubeList.add(new ModelBox(legLower, 0, 22, -2.0F, 3.0F, -4.0F, 4, 0, 6, 0.0F, false));
		legLower.cubeList.add(new ModelBox(legLower, 0, 10, -1.0F, 2.0F, -2.0F, 2, 1, 3, 0.0F, false));
		legLower.cubeList.add(new ModelBox(legLower, 18, 14, -1.0F, 0.0F, -1.0F, 2, 2, 2, 0.0F, false));

		horn1a = new RendererModel(this);
		horn1a.setRotationPoint(-2.5F, 13.0F, -0.5F);
		setAngles(horn1a, 0.0F, 0.0F, -0.4363F);
		horn1a.cubeList.add(new ModelBox(horn1a, 6, 18, -0.5F, -1.5F, -1.0F, 1, 2, 2, 0.0F, false));

		horn1b = new RendererModel(this);
		horn1b.setRotationPoint(-2.5F, 12.0F, -0.5F);
		setAngles(horn1b, 0.0F, 0.0F, 0.2618F);
		horn1b.cubeList.add(new ModelBox(horn1b, 16, 18, -1.0F, -2.0F, -0.5F, 1, 2, 1, 0.0F, false));

		horn2a = new RendererModel(this);
		horn2a.setRotationPoint(2.5F, 13.0F, -0.5F);
		setAngles(horn2a, 0.0F, 3.1416F, 0.4363F);
		horn2a.cubeList.add(new ModelBox(horn2a, 0, 18, -0.5F, -1.5F, -1.0F, 1, 2, 2, 0.0F, false));

		horn2b = new RendererModel(this);
		horn2b.setRotationPoint(2.5F, 12.0F, -0.5F);
		setAngles(horn2b, 0.0F, 0.0F, -0.2618F);
		horn2b.cubeList.add(new ModelBox(horn2b, 12, 18, 0.0F, -2.0F, -0.5F, 1, 2, 1, 0.0F, false));
	}

	@Override
	public void render(FomorEntity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks,
			final float netHeadYaw, final float headPitch, final float scale) {
		body.render(scale);
		leg.render(scale);
		horn1a.render(scale);
		horn1b.render(scale);
		horn2a.render(scale);
		horn2b.render(scale);
	}

	@Override
	public void setRotationAngles(final FomorEntity entity, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
		// leg
		final float speed = 0.9F;
		final float angle = 1.0F;
		// leg angle 1
		final float legAngle = MathHelper.cos(limbSwing * speed) * angle * limbSwingAmount;
		legUpper.rotateAngleX = legUpperAngleX + legAngle;
		legLower.rotateAngleX = legLowerAngleX + legAngle;
		// arms
		this.arm1.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.arm2.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.arm1.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.arm2.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}

	private static void setAngles(RendererModel RendererModel, float x, float y, float z) {
		RendererModel.rotateAngleX = x;
		RendererModel.rotateAngleY = y;
		RendererModel.rotateAngleZ = z;
	}
}