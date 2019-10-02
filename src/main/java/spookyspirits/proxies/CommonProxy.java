package spookyspirits.proxies;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
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
		
	}
	
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
