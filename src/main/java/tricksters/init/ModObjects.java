package tricksters.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.registries.ObjectHolder;
import tricksters.effect.PhookaEffect;
import tricksters.entity.*;

@ObjectHolder(Tricksters.MODID)
public final class ModObjects {
	
	private ModObjects() {}
	
	@ObjectHolder("wisp")
	public static final EntityType<WispEntity> WISP = 
		EntityType.Builder.create(WispEntity::new, EntityClassification.MONSTER)
		.size(0.9F, 1.85F)
		.build("wisp");
	
	@ObjectHolder("willowisp")
	public static final EntityType<WillOWispEntity> WILL_O_WISP = 
		EntityType.Builder.create(WillOWispEntity::new, EntityClassification.MONSTER)
		.size(0.5F, 0.5F).immuneToFire()
		.build("willowisp");
	
	@ObjectHolder("possessed_pumpkin")
	public static final EntityType<PossessedPumpkinEntity> POSSESSED_PUMPKIN = 
		EntityType.Builder.create(PossessedPumpkinEntity::new, EntityClassification.MONSTER)
		.size(0.98F, 1.2F)
		.build("possessed_pumpkin");
	
	@ObjectHolder("phooka")
	public static final EntityType<PhookaEntity> PHOOKA = 
		EntityType.Builder.create(PhookaEntity::new, EntityClassification.CREATURE)
		.size(0.85F, 2.3F)
		.build("phooka");
	
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
	
	@ObjectHolder(PhookaEffect.Squid.NAME)
	public static final Effect PHOOKA_CURSE_SQUID = null;

}
