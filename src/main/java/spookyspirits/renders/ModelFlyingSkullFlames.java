package spookyspirits.renders;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import spookyspirits.entity.FlyingSkull;

public class ModelFlyingSkullFlames extends EntityModel<FlyingSkull> {
	
	private final RendererModel flame1;
	private final RendererModel flame2;
	private final RendererModel flame3;
	private final RendererModel flame4;

	public ModelFlyingSkullFlames() {
		textureWidth = 16;
		textureHeight = 128;

		flame1 = new RendererModel(this);
		flame1.setRotationPoint(0.0F, 24.0F, -6.0F);
		setAngles(flame1, 1.4835F, 0.0F, 0.0F);
		flame1.cubeList.add(new ModelBox(flame1, -12, 0, -6.0F, 1.0F, 0.0F, 12, 0, 16, 0.0F, false));

		flame2 = new RendererModel(this);
		flame2.setRotationPoint(0.0F, 24.0F, 6.0F);
		setAngles(flame2, 1.4835F, 3.1416F, 0.0F);
		flame2.cubeList.add(new ModelBox(flame2, -12, 0, -6.0F, 1.0F, 0.0F, 12, 0, 16, 0.0F, false));

		flame3 = new RendererModel(this);
		flame3.setRotationPoint(-5.0F, 24.0F, 0.0F);
		setAngles(flame3, 1.4835F, 1.5708F, 0.0F);
		flame3.cubeList.add(new ModelBox(flame3, -12, 0, -6.0F, 0.0F, 0.0F, 12, 0, 16, 0.0F, false));

		flame4 = new RendererModel(this);
		flame4.setRotationPoint(-5.0F, 24.0F, 0.0F);
		setAngles(flame4, 1.4835F, -1.5708F, 0.0F);
		flame4.cubeList.add(new ModelBox(flame4, -12, 0, -6.0F, -10.0F, -1.0F, 12, 0, 16, 0.0F, false));
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(final FlyingSkull entityIn, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
				
		this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.setFlameVisible(entityIn.isFlaming());
		GlStateManager.pushMatrix();
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);

		flame1.render(scale);
		flame2.render(scale);
		flame3.render(scale);
		flame4.render(scale);

		GlStateManager.popMatrix();
	}

	private static void setAngles(final RendererModel box, final float x, final float y, final float z) {
		box.rotateAngleX = x;
		box.rotateAngleY = y;
		box.rotateAngleZ = z;
	}
	
	public void setFlameVisible(final boolean isFlaming) {
		flame1.isHidden = !isFlaming;
		flame2.isHidden = !isFlaming;
		flame3.isHidden = !isFlaming;
		flame4.isHidden = !isFlaming;
	}
}
