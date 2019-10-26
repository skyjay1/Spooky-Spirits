package spookyspirits.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import spookyspirits.proxies.ClientProxy;
import spookyspirits.proxies.CommonProxy;
import spookyspirits.util.CPhookaGuiPacket;
import spookyspirits.util.PhookaRiddles;

@Mod(SpookySpirits.MODID)
@Mod.EventBusSubscriber(modid = SpookySpirits.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpookySpirits {
	
	public static final String MODID = "spookyspirits";
	
	public static final CommonProxy PROXY = DistExecutor.runForDist(() -> () -> new ClientProxy(),
			() -> () -> new CommonProxy());

	public static final ItemGroup TAB = new ItemGroup(MODID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Blocks.DIRT);
		}
	};

	public static final Logger LOGGER = LogManager.getFormatterLogger(MODID);
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(MODID, "channel"))
            .networkProtocolVersion(() -> MODID)
            .clientAcceptedVersions(MODID::equals)
            .serverAcceptedVersions(MODID::equals)
            .simpleChannel();
	
	public SpookySpirits() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SpiritsConfig.SPEC);
		// Packets
		CHANNEL.messageBuilder(CPhookaGuiPacket.class, 0)
			.encoder(CPhookaGuiPacket::toBytes)
			.decoder(CPhookaGuiPacket::fromBytes)
			.consumer(CPhookaGuiPacket::handlePacket)
			.add();
	}	
	
	@SubscribeEvent
	public static void onServerStarting(final FMLServerStartingEvent event) {
		LOGGER.info(MODID + ": RegisterSpawns");
		PROXY.registerEntitySpawns();
	}
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		LOGGER.debug(MODID + ": RegisterBlocks");
		PROXY.registerBlocks(event);
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		LOGGER.debug(MODID + ": RegisterItems");
		PROXY.registerItems(event);
	}
	
	@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
		LOGGER.debug(MODID + ": RegisterEntityType");
		PROXY.registerEntities(event);
		PROXY.registerEntityRenders();
	}
	
	@SubscribeEvent
	public static void registerTileEntity(final RegistryEvent.Register<ContainerType<?>> event) {
		LOGGER.debug(MODID + ": RegisterContainerTypes");
		PROXY.registerContainerTypes(event);
	}
	
	@SubscribeEvent
	public static void registerBiome(final RegistryEvent.Register<Biome> event) {
		LOGGER.debug(MODID + ": RegisterBiome");
		PROXY.registerBiome(event);
	}
	
	@SubscribeEvent
	public static void registerEffects(final RegistryEvent.Register<Effect> event) {
		LOGGER.debug(MODID + ": RegisterEffects");
		PROXY.registerEffects(event);
		PhookaRiddles.init();
	}
}
