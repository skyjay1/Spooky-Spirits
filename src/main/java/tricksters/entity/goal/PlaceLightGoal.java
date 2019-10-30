package tricksters.entity.goal;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import tricksters.block.BlockWispLight;
import tricksters.entity.ILightEntity;
import tricksters.init.ModObjects;
import tricksters.init.Tricksters;

public class PlaceLightGoal extends Goal {
	private final LivingEntity entity;
	private final BlockState state;
			
	public PlaceLightGoal(final LivingEntity entityIn, final int lightLevel) {
		entity = entityIn;
		state = ModObjects.WISP_LIGHT.getDefaultState()
				.with(BlockWispLight.LIGHT_LEVEL, lightLevel);
		if(!(entityIn instanceof ILightEntity)) {
			Tricksters.LOGGER.error("Entities given the PlaceLightGoal should implement ILightEntity!" +
		" Tried adding goal for non-compliant class " + entityIn.getClass().getName());
		}
	}

	@Override
	public boolean shouldExecute() {
		return entity.ticksExisted % BlockWispLight.DEF_TICK_RATE == 0;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}
	
	@Override
	public void startExecuting() {
		final BlockPos pos = getPlaceablePos(entity.getPosition());
		if(pos != null) {
			final boolean waterlogged = entity.getEntityWorld().hasWater(pos);
			entity.getEntityWorld().setBlockState(pos, 
					state.with(BlockStateProperties.WATERLOGGED, waterlogged), 2);
		}
	}
	
	@Nullable
	private BlockPos getPlaceablePos(final BlockPos origin) {
		final int[] ia = { 0, -1, 1 };
		for(int x : ia) {
			for(int y : ia) {
				for(int z : ia) {
					final BlockPos p = origin.add(x, y, z);
					// if it's already a light, don't do anything else
					if(entity.getEntityWorld().getBlockState(p).getBlock() == ModObjects.WISP_LIGHT) {
						return null;
					} else if(isReplaceablePos(p)) {
						// if it's not a light but it is replaceable, it's a valid position
						return p;
					}
				}
			}
		}
		
		return null;
	}
	
	private boolean isReplaceablePos(final BlockPos pos) {
		final BlockState s = entity.getEntityWorld().getBlockState(pos);
		return entity.getEntityWorld().isAirBlock(pos) || 
				(s.getBlock() == Blocks.WATER && s.get(FlowingFluidBlock.LEVEL) == 0);
	}
}