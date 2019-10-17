package spookyspirits.proxies;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import spookyspirits.block.BlockWispLight;
import spookyspirits.block.SpoiledBerryBush;
import spookyspirits.effect.PhookaEffect;
import spookyspirits.entity.FlyingSkull;
import spookyspirits.entity.FomorEntity;
import spookyspirits.entity.PhookaEntity;
import spookyspirits.entity.PossessedPumpkinEntity;
import spookyspirits.entity.WillOWispEntity;
import spookyspirits.entity.WispEntity;
import spookyspirits.init.ModObjects;
import spookyspirits.init.SpookySpirits;
import spookyspirits.item.SunshineScrollItem;

public class CommonProxy {
	
	public void registerBiome(final RegistryEvent.Register<Biome> event) {
		
	}

	public void registerBlocks(final RegistryEvent.Register<Block> event) {
		event.getRegistry().register(
				new BlockWispLight(Block.Properties.create(Material.GLASS), 4)
				.setRegistryName(SpookySpirits.MODID, "wisp_light"));
		event.getRegistry().register(
				new SpoiledBerryBush(Block.Properties.from(Blocks.SWEET_BERRY_BUSH))
				.setRegistryName(SpookySpirits.MODID, "spoiled_berry_bush"));
	}

	public void registerItems(final RegistryEvent.Register<Item> event) {
		// create food
		final Food spoiledBerryFood = new Food.Builder().hunger(2).saturation(0.1F)
				.effect(new EffectInstance(Effects.POISON, 110, 0), 0.9F)
				.effect(new EffectInstance(Effects.HUNGER, 180, 0), 1.0F)
				.effect(new EffectInstance(Effects.NAUSEA, 200, 1), 1.0F).build();
		// actually register items
		event.getRegistry().registerAll(
				new BlockNamedItem(ModObjects.SPOILED_BERRY_BUSH, 
					new Item.Properties().group(SpookySpirits.TAB).food(spoiledBerryFood))
				.setRegistryName(SpookySpirits.MODID, "spoiled_berries"),
				new SunshineScrollItem().setRegistryName(SpookySpirits.MODID, "sunshine_scroll"));
	}
	
	public void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
		event.getRegistry().register(
			EntityType.Builder.create(FlyingSkull::new, EntityClassification.MONSTER)
				.size(0.5F, 0.75F).setShouldReceiveVelocityUpdates(true).immuneToFire()
				.build("flying_skull").setRegistryName(SpookySpirits.MODID, "flying_skull")
		);
		
		event.getRegistry().register(
			EntityType.Builder.create(WillOWispEntity::new, EntityClassification.MONSTER)
				.size(0.5F, 0.5F).immuneToFire()
				.build("willowisp").setRegistryName(SpookySpirits.MODID, "willowisp")
		);
		
		event.getRegistry().register(
				EntityType.Builder.create(PossessedPumpkinEntity::new, EntityClassification.MONSTER)
					.size(0.98F, 1.2F)
					.build("possessed_pumpkin").setRegistryName(SpookySpirits.MODID, "possessed_pumpkin")
		);
		
		event.getRegistry().register(
				EntityType.Builder.create(WispEntity::new, EntityClassification.MONSTER)
					.size(0.8F, 2.2F)
					.build("wisp").setRegistryName(SpookySpirits.MODID, "wisp")
		);
		
		event.getRegistry().register(
				EntityType.Builder.create(PhookaEntity::new, EntityClassification.CREATURE)
					.size(0.8F, 1.4F)
					.build("phooka").setRegistryName(SpookySpirits.MODID, "phooka")
		);
		
		event.getRegistry().register(
				EntityType.Builder.create(FomorEntity::new, EntityClassification.WATER_CREATURE)
					.size(0.35F, 0.85F).immuneToFire()
					.build("fomor").setRegistryName(SpookySpirits.MODID, "fomor")
		);
		
		registerEntitySpawns(); // TODO call this from somewhere else?
	}
	
	protected void registerEntitySpawns() {
		// PhookaEntity entity spawn info
//		EntitySpawnPlacementRegistry.register(ModObjects.PHOOKA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
//				Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PhookaEntity::canSpawnHere);
//		final Set<Biome> forestBiomes = new HashSet<>();
//		forestBiomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
//		forestBiomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
//		forestBiomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.JUNGLE));
//		for(final Biome b : forestBiomes) {
//			b.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(ModObjects.PHOOKA, 40, 1, 1));
//		}
		// Possessed Pumpkin entity spawn info
		//EntitySpawnPlacementRegistry.register(ModObjects.POSSESSED_PUMPKIN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
		//		Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PossessedPumpkinEntity::canSpawnHere);
	}
	
	public void registerEntityRenders() { }
	
	public void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event) {
		
	}

	public void registerEffects(final RegistryEvent.Register<Effect> event) {
		event.getRegistry().registerAll(
				new PhookaEffect.Invisibility(),
				new PhookaEffect.Sponge(),
				new PhookaEffect.Footsteps(),
				new PhookaEffect.Eggs()
		);
	}

//	private static final Item makeItem(final String name) {
//		return new Item(new Item.Properties().group(SpookySpirits.TAB)).setRegistryName(SpookySpirits.MODID, name);
//	}
//
//	private static final BlockItem makeIB(final Block base) {
//		BlockItem ib = new BlockItem(base, new Item.Properties().group(SpookySpirits.TAB));
//		ib.setRegistryName(base.getRegistryName());
//		return ib;
//	}
}
