package tricksters.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team.CollisionRule;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import tricksters.proxies.ClientProxy;
import tricksters.proxies.CommonProxy;
import tricksters.util.CPhookaGuiPacket;
import tricksters.util.PhookaRiddles;

@Mod(Tricksters.MODID)
@Mod.EventBusSubscriber(modid = Tricksters.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Tricksters {
	
	public static final String MODID = "tricksters"; // change to "tricksters"
	
	public static final CommonProxy PROXY = DistExecutor.runForDist(() -> () -> new ClientProxy(),
			() -> () -> new CommonProxy());

	public static final Logger LOGGER = LogManager.getFormatterLogger(MODID);
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(MODID, "channel"))
            .networkProtocolVersion(() -> MODID)
            .clientAcceptedVersions(MODID::equals)
            .serverAcceptedVersions(MODID::equals)
            .simpleChannel();
	
	public Tricksters() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, TricksterConfig.SPEC);
		// Packets
		CHANNEL.messageBuilder(CPhookaGuiPacket.class, 0)
			.encoder(CPhookaGuiPacket::toBytes)
			.decoder(CPhookaGuiPacket::fromBytes)
			.consumer(CPhookaGuiPacket::handlePacket)
			.add();
	}	
	
//	@SubscribeEvent
//	public static void onServerStarting(final FMLServerStartingEvent event) {
//		LOGGER.info(MODID + ": RegisterSpawns");
//		PROXY.registerEntitySpawns();
//	}
	
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
	
	/**
	 * Adds the entity to a team that has all entity collision disabled
	 * @param entity the entity
	 **/
	public static void disableCollisionForEntity(final LivingEntity entity) {
		final String TEAM = "nocollision";
		if(entity.getEntityWorld().getScoreboard().getTeam(TEAM) == null) {
			entity.getEntityWorld().getScoreboard().createTeam(TEAM);
			entity.getEntityWorld().getScoreboard().getTeam(TEAM).setCollisionRule(CollisionRule.NEVER);
		}
		final ScorePlayerTeam t = entity.getEntityWorld().getScoreboard().getTeam(TEAM);
		entity.getEntityWorld().getScoreboard().addPlayerToTeam(entity.getCachedUniqueIdString(), t);
	}
}
