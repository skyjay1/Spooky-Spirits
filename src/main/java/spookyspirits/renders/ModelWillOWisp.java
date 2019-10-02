package spookyspirits.renders;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
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
		cube.cubeList.add(new ModelBox(cube, 0, 0, -3.0F, -11.0F, -3.0F, 6, 6, 6, 0.0F, false));
	}

	@Override
	public void render(final WillOWisp entityIn, final float limbSwing, final float limbSwingAmount,
			final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
		cube.render(scale);
	}
}
