package spookyspirits.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;
import spookyspirits.entity.*;

@ObjectHolder(SpookySpirits.MODID)
public final class ModObjects {
	
	private ModObjects() {}
	
	@ObjectHolder("wisp")
	public static final EntityType<WispEntity> WISP = null;
	
	@ObjectHolder("willowisp")
	public static final EntityType<WillOWispEntity> WILL_O_WISP = null;
	
	@ObjectHolder("possessed_pumpkin")
	public static final EntityType<PossessedPumpkinEntity> POSSESSED_PUMPKIN = null;
	
	@ObjectHolder("phooka")
	public static final EntityType<PhookaEntity> PHOOKA = null;
	
	@ObjectHolder("wisp_light")
	public static final Block WISP_LIGHT = null;
	
	@ObjectHolder("spoiled_berry_bush")
	public static final Block SPOILED_BERRY_BUSH = null;
	
	@ObjectHolder("spoiled_berries")
	public static final Item SPOILED_BERRIES = null;

}