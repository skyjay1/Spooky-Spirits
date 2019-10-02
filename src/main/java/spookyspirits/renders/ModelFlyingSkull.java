package spookyspirits.renders;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import spookyspirits.entity.FlyingSkull;

public class ModelFlyingSkull extends EntityModel<FlyingSkull> {
	
	private final RendererModel head;

	public ModelFlyingSkull() {
		textureWidth = 64;
		textureHeight = 32;

		head = new RendererModel(this);
		head.setRotationPoint(0.0F, 24.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -9.0F, -4.0F, 8, 8, 8, 0.0F, false));
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(final FlyingSkull entityIn, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
				
		this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		head.render(scale);
	}

	@Override
	public void setRotationAngles(final FlyingSkull entity, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {

	}
}
