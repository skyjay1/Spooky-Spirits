package spookyspirits.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.registries.ObjectHolder;
import spookyspirits.effect.PhookaEffect;
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
	
	@ObjectHolder("sunshine_scroll")
	public static final Item SUNSHINE_SCROLL = null;
	
	@ObjectHolder(PhookaEffect.Invisibility.NAME)
	public static final Effect PHOOKA_BLESSING_INVISIBILITY = null;
	
	@ObjectHolder(PhookaEffect.Footsteps.NAME)
	public static final Effect PHOOKA_CURSE_FOOTSTEPS = null;

	@ObjectHolder(PhookaEffect.Sponge.NAME)
	public static final Effect PHOOKA_CURSE_SPONGE = null;
	
	@ObjectHolder(PhookaEffect.Eggs.NAME)
	public static final Effect PHOOKA_CURSE_EGGS = null;
	


}
