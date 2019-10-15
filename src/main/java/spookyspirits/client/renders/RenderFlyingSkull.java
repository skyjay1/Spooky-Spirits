package spookyspirits.client.renders;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import spookyspirits.client.models.ModelFlyingSkull;
import spookyspirits.entity.FlyingSkull;

public class RenderFlyingSkull extends LivingRenderer<FlyingSkull, ModelFlyingSkull> {

	private static final ResourceLocation SKULL_SKELETON = new ResourceLocation("minecraft:textures/entity/skeleton/skeleton.png");

	public RenderFlyingSkull(EntityRendererManager manager) {
		super(manager, new ModelFlyingSkull(), 0.25F);
//		this.addLayer(new LayerFlame(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(final FlyingSkull entityIn) {
		return SKULL_SKELETON;
	}
//
//	public static class LayerFlame extends LayerRenderer<FlyingSkull, ModelFlyingSkull> {
//		
//		private static final ResourceLocation CAMPFIRE = new ResourceLocation("minecraft:textures/block/campfire_fire.png");
//		private static final int FRAME_HEIGHT = 16;
//		private static final int NUM_FRAMES = 8;
//
//		public LayerFlame(IEntityRenderer<FlyingSkull, ModelFlyingSkull> i) {
//			super(i);
//		}
//
//		@Override
//		public void render(final FlyingSkull entityIn, final float limbSwing, final float limbSwingAmount,
//				final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, 
//				final float scale) {
//			this.bindTexture(CAMPFIRE);
//			if(entityIn.isFlaming()) {
//				int flameIndex = (entityIn.ticksExisted / 8) % NUM_FRAMES;
//				this.getEntityModel().setFlameTextureY(flameIndex * FRAME_HEIGHT);
//				this.getEntityModel().renderFlames(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//			}
//			this.bindTexture(SKULL_SKELETON);
//			this.getEntityModel().renderSkull(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//		}
//
//		@Override
//		public boolean shouldCombineTextures() {
//			return false;
//		}
//		
//	}

}
