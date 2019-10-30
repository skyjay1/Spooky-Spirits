package tricksters.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tricksters.entity.ILightEntity;

/**
 * Previously written code, adapted from Extra Golems mod
 * @author sky01
 **/
public class BlockWispLight extends Block implements IBucketPickupHandler, ILiquidContainer {

	public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light", 0, 15);
	public static final int DEF_TICK_RATE = 3;
	private final int tickRate;

	public BlockWispLight(final Properties prop, final int light, final int tickrate) {
		super(prop.hardnessAndResistance(-1F).doesNotBlockMovement().tickRandomly().lightValue(light));
		this.setDefaultState(this.getDefaultState());
		this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.WATERLOGGED, false).with(LIGHT_LEVEL, light));
		this.tickRate = tickrate;
	}
	
	public BlockWispLight(final Properties prop, final int light) {
		this(prop, light, DEF_TICK_RATE);
	}

	protected boolean remove(final World worldIn, final BlockState state, final BlockPos pos, final int flag) {
		// remove this block and replace with air (or water)
		final BlockState replaceWith = state.get(BlockStateProperties.WATERLOGGED)
			? Fluids.WATER.getStillFluid().getDefaultState().getBlockState()
			: Blocks.AIR.getDefaultState();
		// replace with air OR water depending on waterlogged state
		return worldIn.setBlockState(pos, replaceWith, flag);
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		// make a slightly expanded AABB to check for the golem
		final AxisAlignedBB toCheck = new AxisAlignedBB(pos).grow(0.5D);
		final List<LivingEntity> list = worldIn.getEntitiesWithinAABB(LivingEntity.class, toCheck);
		// check if list contains an ILightEntity
		boolean shouldStay = false;
		int lightLevel = 0;
		for(final LivingEntity e : list) {
			if(e instanceof ILightEntity) {
				shouldStay = true;
				lightLevel = MathHelper.clamp(((ILightEntity)e).getLightLevel(), 0, 15);
				break;
			}
		}

		if (shouldStay) {
			// update light if necessary
			if(state.get(LIGHT_LEVEL).intValue() != lightLevel) {
				worldIn.setBlockState(pos, state.with(LIGHT_LEVEL, lightLevel), 2);
			}
			// schedule another update later
			worldIn.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), this.tickRate(worldIn));
		} else {
			// remove immediately
			this.remove(worldIn, state, pos, 3);
		}
	}

	@Override
	public int getLightValue(final BlockState state) {
		return state.get(LIGHT_LEVEL);
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.WATERLOGGED);
		builder.add(LIGHT_LEVEL);
	}

	@Override
	public Fluid pickupFluid(final IWorld worldIn, final BlockPos pos, final BlockState state) {
		if (state.get(BlockStateProperties.WATERLOGGED)) {
			worldIn.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)), 3);
			return Fluids.WATER;
		} else {
			return Fluids.EMPTY;
		}
	}

	@Override
	public IFluidState getFluidState(final BlockState state) {
		return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false)
			: super.getFluidState(state);
	}

	@Override
	public boolean canContainFluid(final IBlockReader worldIn, final BlockPos pos, final BlockState state, final Fluid fluidIn) {
		return !state.get(BlockStateProperties.WATERLOGGED) && fluidIn == Fluids.WATER;
	}

	@Override
	public boolean receiveFluid(final IWorld worldIn, final BlockPos pos, final BlockState state, final IFluidState fluidStateIn) {
		if (!state.get(BlockStateProperties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
			if (!worldIn.isRemote()) {
				worldIn.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
				worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(),
					fluidStateIn.getFluid().getTickRate(worldIn));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onBlockAdded(final BlockState state, final World worldIn, final BlockPos pos, final BlockState oldState, final boolean isMoving) {
		if(this.ticksRandomly(state)) {
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
			worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
			worldIn.notifyNeighbors(pos, this);
		}
	}

	@Override
	public void randomTick(final BlockState state, final World worldIn, final BlockPos pos, final Random rand) {
		this.tick(state, worldIn, pos, rand);
	}

	@Override
	public int tickRate(final IWorldReader worldIn) {
		return this.ticksRandomly ? tickRate : super.tickRate(worldIn);
	}
	
	@Override
	public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext cxt) {
		return VoxelShapes.empty();
	}
	
	@Override
	public VoxelShape getCollisionShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext cxt) {
		return VoxelShapes.empty();
	}
	
	@Override
	public VoxelShape getRaytraceShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos) {
		return VoxelShapes.empty();
	}
	
	@Override
	public VoxelShape getRenderShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos) {
		return VoxelShapes.empty();
	}
	
	@Override
	public boolean isNormalCube(final BlockState state, final IBlockReader worldIn, final BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isSolid(final BlockState state) {
		return false;
	}
	
	@Override
	public ItemStack getItem(final IBlockReader worldIn, final BlockPos pos, final BlockState state) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isReplaceable(final BlockState state, final BlockItemUseContext useContext) {
		return true;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockItemUseContext context) {
		return getDefaultState();
	}

	@Override
	public BlockState getStateForPlacement(final BlockState state, final Direction facing, final BlockState state2, final IWorld world,
			final BlockPos pos1, final BlockPos pos2, final Hand hand) {
		return getDefaultState();
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public BlockRenderType getRenderType(final BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void onFallenUpon(final World worldIn, final BlockPos pos, final Entity entityIn, final float fallDistance) {
		// do nothing
	}
	
	@Override
	public void onEntityCollision(final BlockState state, final World worldIn, final BlockPos pos, final Entity entity) {
		// do nothing
	}

	@Override
	public void onLanded(final IBlockReader worldIn, final Entity entityIn) {
		// do nothing
	}

	@Override
	public boolean canSpawnInBlock() {
		return true;
	}
}
