package spookyspirits.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import spookyspirits.effect.PhookaEffect;
import spookyspirits.init.ModObjects;
import spookyspirits.init.SpookySpirits;

public final class PhookaRiddles {

	private static final Map<String, PhookaRiddle> REGISTRY = new HashMap<>();
	private static boolean isFrozen = false;

	private PhookaRiddles() {
	}

	@Nullable
	public static PhookaRiddle getByName(final String id) {
		return REGISTRY.containsKey(id) ? REGISTRY.get(id) : null;
	}

	public static PhookaRiddle register(final PhookaRiddle riddle) {
		if (REGISTRY.containsKey(riddle.getName())) {
			SpookySpirits.LOGGER.error("Tried to add duplicate PhookaRiddle with key '" + riddle.getName() + "' - Skipping");
			return riddle;
		}
		return REGISTRY.put(riddle.getName(), riddle);
	}

	public static void registerAll(final PhookaRiddle... riddles) {
		for (final PhookaRiddle r : riddles) {
			register(r);
		}
	}

	/**
	 * @param rand a Random object
	 * @return a randomly chosen PhookaRiddle if any exist, otherwise null
	 **/
	@Nullable
	public static PhookaRiddle getRandom(final Random rand) {
		return getRandom(rand, null);
	}
	
	/**
	 * 
	 * @param rand a Random object
	 * @param difficulty the difficulty type that should be chosen
	 * @return a randomly chosen PhookaRiddle of the chosen type if any exist, otherwise null
	 **/
	@Nullable
	public static PhookaRiddle getRandom(final Random rand, @Nullable final PhookaRiddle.Type difficulty) {
		final List<PhookaRiddle> values = 
				(difficulty == null) ? Lists.newArrayList(REGISTRY.values())
				: REGISTRY.values().stream().filter(r -> r.getDifficulty() == difficulty).collect(Collectors.toList());
		
		if (values.isEmpty()) {
			SpookySpirits.LOGGER.error("Tried to pick a random PhookaRiddle, but failed to find any of type '" 
					+ (difficulty != null ? difficulty.toString() : "null") + "'");
			return null;
		}
		return values.get(rand.nextInt(values.size()));
	}

	//// REGISTER ALL PHOOKA RIDDLES ////
	public static void init() {
		if(isFrozen) {
			return;
		}
		isFrozen = true;
		
		register(PhookaRiddle.Builder.create("air").setAnswer("block.minecraft.air")
				.setOptions("block.minecraft.water", "block.minecraft.oak_leaves", "item.minecraft.egg")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveEffect(Effects.SPEED, 16000, 1)
						.andThen(new PhookaGiveEffect(Effects.SLOW_FALLING, 16000, 0))
				).setCurse(new PhookaGiveEffect(Effects.SLOWNESS, 16000, 1)
					// TODO give player slowness and increase fall damage
				).build());

		register(PhookaRiddle.Builder.create("darkness").setAnswer("gui.darkness")
				.setOptions("block.minecraft.glass", "block.minecraft.air", "gui.light")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveEffect(PhookaEffect.Invisibility.REGISTRY_NAME, 50000, 0))
				.setCurse(new PhookaGiveEffect(Effects.BLINDNESS, 1200, 1)).build());

		register(PhookaRiddle.Builder.create("coal").setAnswer("item.minecraft.coal")
				.setOptions("block.minecraft.fire", "block.minecraft.iron_ore", "block.minecraft.furnace")
				.setType(PhookaRiddle.Type.MEDIUM)
				// TODO think of something else?
				.setBlessing(new PhookaGiveEffect(Effects.LUCK, 12000, 1))
				.setCurse(new PhookaGiveEffect(Effects.UNLUCK, 12000, 1)).build());

		register(PhookaRiddle.Builder.create("egg").setAnswer("item.minecraft.egg")
				.setOptions("entity.minecraft.cow", "item.minecraft.bucket", "gui.fish")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.EGG, 16)))
				.setCurse(new PhookaGiveEffect(PhookaEffect.Eggs.REGISTRY_NAME, PhookaEffect.Eggs.INTERVAL * 16 + 2, 0))
				.build());

		register(PhookaRiddle.Builder.create("fire1").setAnswer("block.minecraft.fire")
				.setOptions("gui.fish", "block.minecraft.grass", "gui.darkness")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveEffect(Effects.FIRE_RESISTANCE, 20000, 1))
				.setCurse(p -> p.setFire(15)).build());

		register(PhookaRiddle.Builder.create("map1").setAnswer("item.minecraft.filled_map")
				.setOptions("entity.minecraft.chicken", "biome.minecraft.river", "gui.moon")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.MAP)))
				.setCurse(new PhookaTeleportPlayer(64, 100)).build());

		register(PhookaRiddle.Builder.create("map2").setAnswer("item.minecraft.filled_map")
				.setOptions("block.minecraft.gravel", "gui.rain", "item.minecraft.paper")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.MAP)))
				.setCurse(new PhookaTeleportPlayer(64, 100)).build());

		register(PhookaRiddle.Builder.create("rain").setAnswer("gui.rain")
				.setOptions("gui.sun", "block.minecraft.dirt", "gui.wind")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(ModObjects.SUNSHINE_SCROLL)))
				.setCurse(p -> {
					int duration = 8000 + p.getRNG().nextInt(2000);
					p.getEntityWorld().getWorldInfo().setRainTime(duration);
					p.getEntityWorld().getWorldInfo().setThunderTime(duration * 2 / 3);
					p.getEntityWorld().getWorldInfo().setRaining(true);
					p.getEntityWorld().getWorldInfo().setThundering(true);
					p.getEntityWorld().setRainStrength(1.0F);
					p.getEntityWorld().setThunderStrength(1.0F);
				}).build());

		register(PhookaRiddle.Builder.create("bottle").setAnswer("item.minecraft.glass_bottle")
				.setOptions("entity.minecraft.creeper", "biome.minecraft.river", "item.minecraft.bucket")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(
						PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD),
						PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.WATER)))
				.setCurse(p -> {
					int size = p.inventory.getSizeInventory();
					for(int i = 0; i < size; i++) {
						if(p.inventory.getStackInSlot(i).isEmpty()) {
							p.inventory.setInventorySlotContents(i, new ItemStack(Items.GLASS_BOTTLE, 1));
						}
					}
				}).build());
		
		register(PhookaRiddle.Builder.create("footsteps").setAnswer("subtitles.block.generic.footsteps")
				.setOptions("item.minecraft.wheat_seeds", "gui.shadow", "biome.minecraft.the_void")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveEffect(Effects.SPEED, 20000, 2))
				.setCurse(new PhookaGiveEffect(PhookaEffect.Footsteps.REGISTRY_NAME, 5000, 0)).build());
		
		register(PhookaRiddle.Builder.create("bed").setAnswer("block.minecraft.bed")
				.setOptions("entity.minecraft.wolf", "block.minecraft.chest", "entity.minecraft.llama")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.LIGHT_GRAY_BED)))
				.setCurse(p -> {
					if(p.isServerWorld()) {
						final PhantomEntity phantom = EntityType.PHANTOM.create(p.getEntityWorld());
						phantom.setPosition(p.posX, p.posY + 20, p.posZ);
						phantom.setAttackTarget(p);
						p.getEntityWorld().addEntity(phantom);
					}
				}).build());
		
		register(PhookaRiddle.Builder.create("water").setAnswer("block.minecraft.water")
				.setOptions("entity.minecraft.ocelot", "entity.minecraft.horse", "block.minecraft.fire")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET),
						new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET)))
				.setCurse(new PhookaTeleportToBiome(Biome.Category.OCEAN)).build());
		
		register(PhookaRiddle.Builder.create("hook").setAnswer("gui.hook")
				.setOptions("item.minecraft.bread", "item.minecraft.spider_eye", "block.minecraft.cobweb")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(
						enchant(enchant(new ItemStack(Items.FISHING_ROD), Enchantments.LURE, 2), Enchantments.LUCK_OF_THE_SEA, 3)))
				.setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("coffin").setAnswer("gui.coffin")
				.setOptions("entity.minecraft.boat", "item.minecraft.book", "gui.candle")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.TOTEM_OF_UNDYING)))
				.setCurse(p -> {
					if(p.isServerWorld()) {
						final SkeletonEntity sk = EntityType.SKELETON.create(p.getEntityWorld());
						sk.setPosition(p.posX, p.posY, p.posZ);
						sk.setAttackTarget(p);
						sk.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
						p.getEntityWorld().addEntity(sk);
					}
				}).build());
		
		register(PhookaRiddle.Builder.create("sponge").setAnswer("block.minecraft.sponge")
				.setOptions("gui.fish", "block.minecraft.fire", "item.minecraft.bucket")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(Effects.RESISTANCE, 30000, 1))
				.setCurse(new PhookaGiveEffect(PhookaEffect.Sponge.REGISTRY_NAME, Short.MAX_VALUE, 0)).build());
		
		register(PhookaRiddle.Builder.create("fire2").setAnswer("block.minecraft.fire")
				.setOptions("gui.candle", "biome.minecraft.the_void", "entity.minecraft.husk")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(new PhookaGiveEffect(Effects.FIRE_RESISTANCE, 40000, 1))
				.setCurse(p -> p.setFire(15)).build());
		
		register(PhookaRiddle.Builder.create("fire3").setAnswer("block.minecraft.fire")
				.setOptions("block.minecraft.grass", "entity.minecraft.zombie", "gui.wind")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(Effects.FIRE_RESISTANCE, 30000, 1))
				.setCurse(p -> p.setFire(15)).build());
		
		register(PhookaRiddle.Builder.create("wind").setAnswer("gui.wind")
				.setOptions("entity.minecraft.lightning_bolt", "block.minecraft.fire", "gui.tree")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(Effects.SPEED, 40000, 2))
				.setCurse(new PhookaGiveEffect(Effects.LEVITATION, 600, 0)).build());
		
		register(PhookaRiddle.Builder.create("eye").setAnswer("gui.eye")
				.setOptions("biome.minecraft.river", "biome.minecraft.ocean", "block.minecraft.player_head")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(Effects.NIGHT_VISION, 20000, 0))
				.setCurse(new PhookaGiveEffect(Effects.BLINDNESS, 1000, 0)).build());
		
		register(PhookaRiddle.Builder.create("horse").setAnswer("entity.minecraft.horse")
				.setOptions("entity.minecraft.boat", "entity.minecraft.rabbit", "entity.minecraft.pig")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(p -> {
					if(p.isServerWorld()) {
						int variant = p.getRNG().nextInt(7) |
								p.getRNG().nextInt(5) << 8;
						final HorseEntity horse = EntityType.HORSE.create(p.getEntityWorld());
						horse.setTamedBy(p);
						horse.setRearing(true);
						horse.setHorseVariant(variant);
						horse.replaceItemInInventory(0, new ItemStack(Items.SADDLE));
						horse.setPosition(p.posX, p.posY, p.posZ);
						p.getEntityWorld().addEntity(horse);
					}
				}).setCurse(p -> {
					if(p.isServerWorld()) {
						// skeleton horse
						final SkeletonHorseEntity horse = EntityType.SKELETON_HORSE.create(p.getEntityWorld());
						horse.setRearing(true);
						horse.setHorseTamed(false);
						horse.setPosition(p.posX, p.posY, p.posZ);
						p.getEntityWorld().addEntity(horse);
						// skeleton
						final SkeletonEntity sk = EntityType.SKELETON.create(p.getEntityWorld());
						sk.setPosition(p.posX, p.posY, p.posZ);
						sk.setAttackTarget(p);
						sk.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
						sk.startRiding(horse, true);
						p.getEntityWorld().addEntity(sk);
					} 
				}).build());
		
		register(PhookaRiddle.Builder.create("shadow1").setAnswer("gui.shadow")
				.setOptions("gui.sleep", "entity.minecraft.spider", "entity.minecraft.lightning_bolt")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(PhookaEffect.Invisibility.REGISTRY_NAME, 50000, 3))
				.setCurse(p -> {
					long dayTime = p.getEntityWorld().getDayTime();
					p.getEntityWorld().setDayTime(dayTime - (dayTime % 24000L) - 12000L);
				}).build());
		
		register(PhookaRiddle.Builder.create("candle").setAnswer("gui.candle")
				.setOptions("gui.moon", "block.minecraft.fire", "item.minecraft.arrow")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.TORCH, 64)))
				.setCurse(new PhookaGiveEffect(Effects.GLOWING, 12000, 0))
				.build());
		
		register(PhookaRiddle.Builder.create("shadow2").setAnswer("gui.shadow")
				.setOptions("gui.sleep", "effect.minecraft.hunger", "item.minecraft.filled_map")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("pen").setAnswer("gui.pen")
				.setOptions("item.minecraft.emerald", "item.minecraft.iron_sword", "gui.shadow")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.INK_SAC, 64))).setCurse(p -> {
					// TODO a squid is stuck on your head, limiting your vision
					if(p.isServerWorld()) {
						final SquidEntity squid = EntityType.SQUID.create(p.getEntityWorld());
						squid.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D);
						squid.setHealth(50F);
						squid.setPosition(p.posX, p.posY, p.posZ);
						p.getEntityWorld().addEntity(squid);
						squid.startRiding(p, true);
						p.addShoulderEntity(squid.serializeNBT());
					}
				}).build());
	}
	
	public static void dump() {
		for(final PhookaRiddle r : REGISTRY.values()) {
			SpookySpirits.LOGGER.info(r.toString());
		}
	}
	
	private static ItemStack enchant(final ItemStack i, final Enchantment e, final int l) {
		i.addEnchantment(e, l);
		return i;
	}
	
	public static class PhookaGiveEffect implements Consumer<PlayerEntity> {
		
		private final ResourceLocation effectName;
		private final int duration;
		private final int amplifier;
		
		public PhookaGiveEffect(final ResourceLocation effectID, final int length, final int amp) {
			effectName = effectID;
			duration = length;
			amplifier = amp;
		}
		
		public PhookaGiveEffect(final Effect e, final int length, final int amp) {
			this(e.getRegistryName(), length, amp);
		}
		
		@Override
		public void accept(final PlayerEntity t) {
			// TODO decide whether to show particles (change last arg to 'true')
			if(t.isServerWorld() && !t.getEntityWorld().isRemote) {
				final Effect effect = ForgeRegistries.POTIONS.getValue(effectName);
				t.addPotionEffect(new EffectInstance(effect, duration, amplifier, false, !(effect instanceof PhookaEffect)));
			}
		}
	}
	
	public static class PhookaGiveItem implements Consumer<PlayerEntity> {
		
		private final ItemStack[] items;
		
		public PhookaGiveItem(final ItemStack... i) {
			items = i;
		}
		@Override
		public void accept(final PlayerEntity t) {
			for(final ItemStack i : items) {
				t.addItemStackToInventory(i);
			}
		}
	}
	
	public static class PhookaTeleportPlayer implements Consumer<PlayerEntity> {

		private final int min;
		private final int max;

		public PhookaTeleportPlayer(final int minRange, final int maxRange) {
			min = minRange;
			max = maxRange;
		}

		@Override
		public void accept(final PlayerEntity t) {
			attemptTeleportAround(t, t.getPosition());
		}
		
		protected boolean attemptTeleportAround(final PlayerEntity t, final BlockPos origin) {
			int attemptsLeft = 25;
			BlockPos pos = origin;
			int x;
			int z;
			// try to teleport the player until it works, or you fail too many times
			do {
				x = min + t.getRNG().nextInt(max);
				z = min + t.getRNG().nextInt(max);
				if (t.getRNG().nextBoolean()) {
					x *= -1;
				}
				if (t.getRNG().nextBoolean()) {
					z *= -1;
				}
				pos = getFloorY(t.getEntityWorld(), origin.add(x, 40, z));
			} while (attemptsLeft-- > 0 && t.attemptTeleport(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, false));

			if (attemptsLeft > 0) {
				// assume success
				t.getEntityWorld().playSound((PlayerEntity) null, pos.getX(), pos.getY(), pos.getZ(),
						SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				t.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
				return true;
			}
			return false;
		}

		protected static BlockPos getFloorY(final World world, BlockPos p) {
			while (p.getY() > 255 || (world.isAirBlock(p.down()) && p.getY() > 0)) {
				p = p.down();
			}
			return p;
		}
	}
	
	public static class PhookaTeleportToBiome extends PhookaTeleportPlayer {
		
		private final Biome.Category type;

		public PhookaTeleportToBiome(Biome.Category biomeType) {
			super(1, 10);
			type = biomeType;
		}
		
		@Override
		public void accept(final PlayerEntity p) {
			final BlockPos origin = p.getPosition();
			// check ever-increasing circles until a max radius of 4000
			for(float radius = 32; radius < 4000; radius += 16) {
				final double dt = Math.PI / Math.max(4.0F, radius / 32.0F);
				// cycle through each angle in a full circle
				for(double t = 0; t < 2 * Math.PI; t += dt) {
					// determine the location based on angle and radius
					int x = (int)(radius * Math.sin(t));
					int z = (int)(radius * Math.cos(t));
					BlockPos pos = origin.add(x, 0, z);
					// check if this location is the correct biome
					if(p.getEntityWorld().getBiome(pos).getCategory() == this.type) {
						// we found the biome! teleport the player to a safe location there
						if(this.attemptTeleportAround(p, pos)) {
							return;
						}
					}
				}
			}
			// if we finished the loop, that means we didn't find any of that biome
			// so just teleport the player randomly
			super.accept(p);
		}
		
	}
}
