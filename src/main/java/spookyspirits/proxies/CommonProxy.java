package spookyspirits.proxies;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import spookyspirits.entity.FlyingSkull;
import spookyspirits.entity.PossessedPumpkin;
import spookyspirits.entity.WillOWisp;
import spookyspirits.init.SpookySpirits;

public class CommonProxy {
	
	public void registerBiome(final RegistryEvent.Register<Biome> event) {
		
	}

	public void registerBlocks(final RegistryEvent.Register<Block> event) {
		
	}

	public void registerItems(final RegistryEvent.Register<Item> event) {
		// Item
		
	}
	
	public void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
		event.getRegistry().register(
			EntityType.Builder.create(FlyingSkull::new, EntityClassification.MONSTER)
				.size(0.5F, 0.75F).setShouldReceiveVelocityUpdates(true).immuneToFire()
				.build("flying_skull").setRegistryName(SpookySpirits.MODID, "flying_skull")
		);
		
		event.getRegistry().register(
			EntityType.Builder.create(WillOWisp::new, EntityClassification.MONSTER)
				.size(0.5F, 0.5F)
				.build("willowisp").setRegistryName(SpookySpirits.MODID, "willowisp")
		);
		
		event.getRegistry().register(
				EntityType.Builder.create(PossessedPumpkin::new, EntityClassification.MONSTER)
					.size(0.99F, 1.2F)
					.build("possessed_pumpkin").setRegistryName(SpookySpirits.MODID, "possessed_pumpkin")
			);
	}
	
	public void registerEntityRenders() { }
	
	public void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event) {
		
	}

	private static final Item makeItem(final String name) {
		return new Item(new Item.Properties().group(SpookySpirits.TAB)).setRegistryName(SpookySpirits.MODID, name);
	}

	private static final BlockItem makeIB(final Block base) {
		BlockItem ib = new BlockItem(base, new Item.Properties().group(SpookySpirits.TAB));
		ib.setRegistryName(base.getRegistryName());
		return ib;
	}
}
