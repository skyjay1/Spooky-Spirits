package spookyspirits.client.renders;

import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import spookyspirits.client.models.ModelPossessedPumpkin;
import spookyspirits.entity.PossessedPumpkinEntity;
import spookyspirits.init.SpookySpirits;

public class RenderPossessedPumpkin extends LivingRenderer<PossessedPumpkinEntity, ModelPossessedPumpkin> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(SpookySpirits.MODID,
			"textures/entities/possessed_pumpkin.png");

	public RenderPossessedPumpkin(EntityRendererManager manager) {
		super(manager, new ModelPossessedPumpkin(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(PossessedPumpkinEntity entity) {
		return TEXTURE;
	}

	@Override
	protected boolean canRenderName(PossessedPumpkinEntity entity) {
		return false;
	}

	@Override
	public void doRender(final PossessedPumpkinEntity entity, final double x, final double y, final double z, 
			final float entityYaw, final float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		// TODO find out how to rotate a drawn block so we can use built-in textures
		//float standing = entity.getStandingTicksRatio() * 4.0F;
		// render head (pumpkin block)
		//GlStateManager.pushMatrix();
		//GlStateManager.translatef(0, standing * 0.0625F, 0);
		//renderBlock(entity, Blocks.CARVED_PUMPKIN.getDefaultState(), x, y, z, entityYaw, partialTicks);
		//GlStateManager.popMatrix();
	}

	@SuppressWarnings("deprecation")
	public void renderBlock(final PossessedPumpkinEntity entity, final BlockState blockstate, final 
			double x, final double y, final double z, final float entityYaw, final float partialTicks) {
		// TODO NOT WORKING WITH ROTATION!
		if (blockstate.getRenderType() == BlockRenderType.MODEL) {
			final World world = entity.getEntityWorld();
			this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			if (this.renderOutlines) {
				GlStateManager.enableColorMaterial();
				GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
			}
			bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
			BlockPos blockpos = new BlockPos(entity.posX, entity.getBoundingBox().maxY, entity.posZ);
			GlStateManager.translatef((float) (x - (double) blockpos.getX() - 0.5D),
					(float) (y - (double) blockpos.getY()), (float) (z - (double) blockpos.getZ() - 0.5D));			
			BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
			blockrendererdispatcher.getBlockModelRenderer().renderModel(world,
					blockrendererdispatcher.getModelForState(blockstate), blockstate, blockpos, bufferbuilder, false,
					new Random(), blockstate.getPositionRandom(entity.getPosition()));
			tessellator.draw();
			if (this.renderOutlines) {
				GlStateManager.tearDownSolidRenderingTextureCombine();
				GlStateManager.disableColorMaterial();
			}

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}

}
