package tricksters.proxies;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import tricksters.block.BlockWispLight;
import tricksters.block.SpoiledBerryBush;
import tricksters.effect.PhookaEffect;
import tricksters.entity.FlyingSkull;
import tricksters.entity.FomorEntity;
import tricksters.entity.PhookaEntity;
import tricksters.entity.PossessedPumpkinEntity;
import tricksters.entity.WillOWispEntity;
import tricksters.entity.WispEntity;
import tricksters.init.ModObjects;
import tricksters.init.Tricksters;
import tricksters.item.SunshineScrollItem;

public class CommonProxy {
	
	public void registerBiome(final RegistryEvent.Register<Biome> event) {
		
	}

	public void registerBlocks(final RegistryEvent.Register<Block> event) {
		event.getRegistry().register(
				new BlockWispLight(Block.Properties.create(Material.GLASS), 4)
				.setRegistryName(Tricksters.MODID, "wisp_light"));
		event.getRegistry().register(
				new SpoiledBerryBush(Block.Properties.from(Blocks.SWEET_BERRY_BUSH))
				.setRegistryName(Tricksters.MODID, "spoiled_berry_bush"));
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
					new Item.Properties().group(ItemGroup.FOOD).food(spoiledBerryFood))
				.setRegistryName(Tricksters.MODID, "spoiled_berries"),
				new SunshineScrollItem().setRegistryName(Tricksters.MODID, "sunshine_scroll"));
	}
	
	public void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
//		event.getRegistry().register(
//			EntityType.Builder.create(FlyingSkull::new, EntityClassification.MONSTER)
//				.size(0.5F, 0.75F).setShouldReceiveVelocityUpdates(true).immuneToFire()
//				.build("flying_skull").setRegistryName(Tricksters.MODID, "flying_skull")
//		);
		
		event.getRegistry().register(
			ModObjects.WILL_O_WISP.setRegistryName(Tricksters.MODID, "willowisp")
		);
		
		event.getRegistry().register(
				ModObjects.POSSESSED_PUMPKIN.setRegistryName(Tricksters.MODID, "possessed_pumpkin")
		);
		
		event.getRegistry().register(
				ModObjects.WISP.setRegistryName(Tricksters.MODID, "wisp")
		);
		
		event.getRegistry().register(
				ModObjects.PHOOKA.setRegistryName(Tricksters.MODID, "phooka")
		);
		
//		event.getRegistry().register(
//				EntityType.Builder.create(FomorEntity::new, EntityClassification.WATER_CREATURE)
//					.size(0.35F, 0.85F).immuneToFire()
//					.build("fomor").setRegistryName(Tricksters.MODID, "fomor")
//		);
		registerEntitySpawns();
	}
	
	public void registerEntitySpawns() {
		// PhookaEntity entity spawn info
		EntitySpawnPlacementRegistry.register(ModObjects.PHOOKA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
				Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PhookaEntity::canSpawnHere);
		// Possessed Pumpkin entity spawn info
		EntitySpawnPlacementRegistry.register(ModObjects.POSSESSED_PUMPKIN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
				Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PossessedPumpkinEntity::canSpawnHere);
		// Wisp entity spawn info
		EntitySpawnPlacementRegistry.register(ModObjects.WISP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
				Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WispEntity::canSpawnHere);
		// biome spawns
		for(final Biome b : ForgeRegistries.BIOMES.getValues()) {
			b.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(ModObjects.WISP, 30, 1, 1));
			b.getSpawns(EntityClassification.MONSTER).add(new Biome.SpawnListEntry(ModObjects.POSSESSED_PUMPKIN, 20, 1, 2));
			if(b.getCategory() == Biome.Category.FOREST) {
				b.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(ModObjects.PHOOKA, 30, 1, 1));
			}
		}
	}
	
	public void registerEntityRenders() { }
	
	public void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event) {
		
	}

	public void registerEffects(final RegistryEvent.Register<Effect> event) {
		event.getRegistry().registerAll(
				new PhookaEffect.Invisibility(),
				new PhookaEffect.Sponge(),
				new PhookaEffect.Footsteps(),
				new PhookaEffect.Eggs(),
				new PhookaEffect.Squid()
		);
	}

//	private static final Item makeItem(final String name) {
//		return new Item(new Item.Properties().group(Tricksters.TAB)).setRegistryName(Tricksters.MODID, name);
//	}
//
//	private static final BlockItem makeIB(final Block base) {
//		BlockItem ib = new BlockItem(base, new Item.Properties().group(Tricksters.TAB));
//		ib.setRegistryName(base.getRegistryName());
//		return ib;
//	}
}
