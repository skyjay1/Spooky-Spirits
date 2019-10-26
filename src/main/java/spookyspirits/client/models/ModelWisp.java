package spookyspirits.client.models;

import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import spookyspirits.entity.WispEntity;

public class ModelWisp extends EntityModel<WispEntity>{
	
	public static final Random rand = new Random(System.currentTimeMillis());

	private static final int SIZE = 6;
	private WispPart[] parts;

	public ModelWisp() {
		super();
		this.textureWidth = 32;
		this.textureHeight = 16;
		parts = new WispPart[WispEntity.NUM_PARTS];
		initParts();
	}

	@Override
	public void render(WispEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		final float size = 0.5F;
		GlStateManager.pushMatrix();
		GlStateManager.scalef(size, size, size);
		GlStateManager.translatef(0, 6F * scale, 0);
		// enable alpha
		GlStateManager.enableBlend();
		GlStateManager.enableNormalize();
		GlStateManager.blendFunc(770, 771);
		// render each part
		for(final WispPart p : parts) {
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, p.f);
			p.model.render(scale);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(WispEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scale) {
//		// bobbing up and down
//		final float bobbing = (float)Math.sin((entityIn.ticksExisted + entityIn.getEntityId()) * 0.12D) * 0.38F;
//		GlStateManager.translatef(0, bobbing * scaleFactor, 0);
		for(final WispPart p : this.parts) {
			p.update(entityIn);
		}
	}
	
	private void initParts() {
		final float MIN_RAD = 0.25F;
		final float RADIUS = 3.8F;
		for(int i = 0; i < WispEntity.NUM_PARTS; i++) {
			// set rotation points
			float x = (rand.nextFloat() + MIN_RAD) * SIZE * RADIUS * (rand.nextBoolean() ? 1F : -1F);
			float y = (rand.nextFloat() + MIN_RAD) * SIZE * RADIUS * (rand.nextBoolean() ? 1F : -1F);
			float z = (rand.nextFloat() + MIN_RAD) * SIZE * RADIUS * (rand.nextBoolean() ? 1F : -1F);
			// set fade factors
			float fi = rand.nextFloat() * 200F;
			float df = rand.nextFloat() * 0.08F;
			this.parts[i] = new WispPart(this, i, x, y, z, fi, df);
		}
	}
	
	static class WispPart {
		
		protected final RendererModel model;
		protected final int i;

		protected final float fi;
		protected float f;
		protected float df;
		
		protected WispPart(final EntityModel<?> baseModel, final int index,
				final float posX, final float posY, final float posZ,
				final float fadeInitial, final float fadeSpeed) {
			i = index;
			df = Math.max(0.01F, fadeSpeed);
			fi = fadeInitial;
			f = 0;
			model = new RendererModel(baseModel);
			model.setRotationPoint(0, rand.nextFloat() * 16.0F, 0);
			model.cubeList.add(new ModelBox(model, 0, 0, posX, posY, posZ, SIZE, SIZE, SIZE, 0.0F, false));
		}
		
		public void update(final WispEntity entity) {
			if(entity.partRotations != null && entity.partMotions != null && i < entity.partMotions.length) {
				model.rotateAngleX = entity.partRotations[i].getX();
				model.rotateAngleY = entity.partRotations[i].getY();
				model.rotateAngleZ = entity.partRotations[i].getZ();
			}
			f = (float)Math.sin((fi + entity.ticksExisted) * df);
		}
	}
}
