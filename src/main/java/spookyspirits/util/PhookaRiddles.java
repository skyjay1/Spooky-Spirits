package spookyspirits.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
			SpookySpirits.LOGGER
					.error("Tried to add duplicate PhookaRiddle with key '" + riddle.getName() + "' - Skipping");
			return riddle;
		}
		return REGISTRY.put(riddle.getName(), riddle);
	}

	public static void registerAll(final PhookaRiddle... riddles) {
		for (final PhookaRiddle r : riddles) {
			register(r);
		}
	}

	@Nullable
	public static PhookaRiddle getRandom(final Random rand) {
		final List<PhookaRiddle> values = new ArrayList<>();
		values.addAll(REGISTRY.values());
		if (values.isEmpty()) {
			SpookySpirits.LOGGER.error("Tried to pick a random PhookaRiddle, but there weren't any registered!");
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
				.setBlessing(p -> {
					// TODO give player speed and reduce fall damage
				}).setCurse(p -> {
					// TODO give player slowness and increase fall damage
				}).build());

		register(PhookaRiddle.Builder.create("darkness").setAnswer("darkness")
				.setOptions("block.minecraft.glass", "block.minecraft.air", "light")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveEffect(ModObjects.PHOOKA_BLESSING_INVISIBILITY, 50000, 0))
				.setCurse(new PhookaGiveEffect(Effects.BLINDNESS, 1200, 1)).build());

		register(PhookaRiddle.Builder.create("coal").setAnswer("item.minecraft.coal")
				.setOptions("block.minecraft.fire", "block.minecraft.iron_ore", "block.minecraft.furnace")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(ModObjects.PHOOKA_BLESSING_FORTUNE, 24000, 0)).setCurse(p -> {
					// TODO give player effect that causes ore block drops to randomly turn to
					// coal??
				}).build());

		register(PhookaRiddle.Builder.create("egg").setAnswer("item.minecraft.egg")
				.setOptions("entity.minecraft.cow", "item.minecraft.bucket", "fish")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.EGG, 16)))
				.setCurse(p -> {
					if(p.isServerWorld() && !p.getEntityWorld().isRemote) {
						// TODO 16 eggs are thrown at player from various directions
					}
				}).build());

		register(PhookaRiddle.Builder.create("fire1").setAnswer("block.minecraft.fire")
				.setOptions("fish", "block.minecraft.grass", "darkness")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveEffect(Effects.FIRE_RESISTANCE, 20000, 1))
				.setCurse(p -> p.setFire(15)).build());

		register(PhookaRiddle.Builder.create("map1").setAnswer("item.minecraft.filled_map")
				.setOptions("entity.minecraft.chicken", "biome.minecraft.river", "moon")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.MAP)))
				.setCurse(new PhookaTeleportPlayer(64, 100)).build());

		register(PhookaRiddle.Builder.create("map2").setAnswer("item.minecraft.filled_map")
				.setOptions("block.minecraft.gravel", "rain", "item.minecraft.paper")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.MAP)))
				.setCurse(new PhookaTeleportPlayer(64, 100)).build());

		register(PhookaRiddle.Builder.create("rain").setAnswer("rain")
				.setOptions("sun", "block.minecraft.dirt", "wind")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(p -> {
					// TODO give player one-use item "Scroll of Sunshine" that stops rain
				}).setCurse(p -> { 
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
					// TODO inventory is filled with empty bottles
				}).build());
		
		register(PhookaRiddle.Builder.create("footsteps").setAnswer("subtitles.block.generic.footsteps")
				.setOptions("item.minecraft.wheat_seeds", "shadow", "biome.minecraft.the_void")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveEffect(Effects.SPEED, 20000, 2))
				.setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("bed").setAnswer("block.minecraft.bed")
				.setOptions("entity.minecraft.wolf", "block.minecraft.chest", "entity.minecraft.llama")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.LIGHT_GRAY_BED)))
				.setCurse(p -> {
					if(p.isServerWorld() && !p.getEntityWorld().isRemote) {
						final PhantomEntity phantom = EntityType.PHANTOM.create(p.getEntityWorld());
						phantom.setPosition(p.posX, p.posY + 20, p.posZ);
						phantom.setAttackTarget(p);
						p.getEntityWorld().addEntity(phantom);
					}
				}).build());
		
		register(PhookaRiddle.Builder.create("water").setAnswer("blocks.minecraft.water")
				.setOptions("entity.minecraft.ocelot", "entity.minecraft.horse", "block.minecraft.fire")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET),
						new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET)))
				.setCurse(p -> {
					// TODO
				}).build());
		
		register(PhookaRiddle.Builder.create("hook").setAnswer("hook")
				.setOptions("item.minecraft.bread", "item.minecraft.spider_eye", "block.minecraft.cobweb")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(
						enchant(enchant(new ItemStack(Items.FISHING_ROD), Enchantments.LURE, 2), Enchantments.LUCK_OF_THE_SEA, 3)))
				.setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("coffin").setAnswer("coffin")
				.setOptions("entity.minecraft.boat", "item.minecraft.book", "candle")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.TOTEM_OF_UNDYING)))
				.setCurse(p -> {
					if(p.isServerWorld() && !p.getEntityWorld().isRemote) {
						final SkeletonEntity sk = EntityType.SKELETON.create(p.getEntityWorld());
						sk.setPosition(p.posX, p.posY, p.posZ);
						sk.setAttackTarget(p);
						p.getEntityWorld().addEntity(sk);
					}
				}).build());
		
		register(PhookaRiddle.Builder.create("sponge").setAnswer("block.minecraft.sponge")
				.setOptions("fish", "block.minecraft.fire", "item.minecraft.bucket")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(Effects.RESISTANCE, 30000, 1))
				.setCurse(new PhookaGiveEffect(ModObjects.PHOOKA_CURSE_SPONGE, Short.MAX_VALUE, 0)).build());
		
		register(PhookaRiddle.Builder.create("fire2").setAnswer("block.minecraft.fire")
				.setOptions("candle", "biome.minecraft.the_void", "entity.minecraft.husk")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(new PhookaGiveEffect(Effects.FIRE_RESISTANCE, 40000, 1))
				.setCurse(p -> p.setFire(15)).build());
		
		register(PhookaRiddle.Builder.create("fire3").setAnswer("block.minecraft.fire")
				.setOptions("block.minecraft.grass", "entity.minecraft.zombie", "wind")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(Effects.FIRE_RESISTANCE, 30000, 1))
				.setCurse(p -> p.setFire(15)).build());
		
		register(PhookaRiddle.Builder.create("wind").setAnswer("wind")
				.setOptions("entity.minecraft.lightning_bolt", "block.minecraft.fire", "tree")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(Effects.SPEED, 40000, 2))
				.setCurse(new PhookaGiveEffect(Effects.LEVITATION, 600, 0)).build());
		
		register(PhookaRiddle.Builder.create("eye").setAnswer("eye")
				.setOptions("biome.minecraft.river", "biome.minecraft.ocean", "block.minecraft.player_head")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(Effects.NIGHT_VISION, 20000, 0))
				.setCurse(new PhookaGiveEffect(Effects.BLINDNESS, 1000, 0)).build());
		
		register(PhookaRiddle.Builder.create("horse").setAnswer("entity.minecraft.horse")
				.setOptions("entity.minecraft.boat", "entity.minecraft.rabbit", "entity.minecraft.pig")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(p -> {
					if(p.isServerWorld() && !p.getEntityWorld().isRemote) {
						int variant = p.getEntityWorld().getRandom().nextInt(7) | 
								p.getEntityWorld().getRandom().nextInt(5) << 8;
						final HorseEntity horse = EntityType.HORSE.create(p.getEntityWorld());
						horse.setTamedBy(p);
						horse.setRearing(true);
						horse.setHorseVariant(variant);
						horse.setHorseSaddled(true);
						horse.setPosition(p.posX, p.posY, p.posZ);
						p.getEntityWorld().addEntity(horse);
					}
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("shadow1").setAnswer("shadow")
				.setOptions("sleep", "entity.minecraft.spider", "entity.minecraft.lightning_bolt")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(new PhookaGiveEffect(ModObjects.PHOOKA_BLESSING_INVISIBILITY, 50000, 3))
				.setCurse(p -> {
					long dayTime = p.getEntityWorld().getDayTime();
					p.getEntityWorld().setDayTime(dayTime - (dayTime % 24000L) - 12000L);
				}).build());
		
		register(PhookaRiddle.Builder.create("candle").setAnswer("candle")
				.setOptions("moon", "block.minecraft.fire", "item.minecraft.arrow")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.TORCH, 64)))
				.setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("shadow2").setAnswer("shadow")
				.setOptions("sleep", "effect.minecraft.hunger", "item.minecraft.filled_map")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("pen").setAnswer("pen")
				.setOptions("item.minecraft.emerald", "entity.minecraft.witch", "shadow")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.INK_SAC, 64))).setCurse(p -> {
					// TODO a squid is stuck on your head, limiting your vision
					if(p.isServerWorld() && !p.getEntityWorld().isRemote) {
						final SquidEntity e = EntityType.SQUID.create(p.getEntityWorld());
						e.setHealth(100F);
						e.setPosition(p.posX, p.posY, p.posZ);
						p.getEntityWorld().addEntity(e);
						e.startRiding(p, true);
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
		
		private final Effect effect;
		private final int duration;
		private final int amplifier;
		
		public PhookaGiveEffect(final Effect e, final int length, final int amp) {
			effect = e;
			duration = length;
			amplifier = amp;
		}
		
		@Override
		public void accept(PlayerEntity t) {
			t.addPotionEffect(new EffectInstance(effect, duration, amplifier));
		}
	}
	
	public static class PhookaGiveItem implements Consumer<PlayerEntity> {
		
		private final ItemStack[] items;
		
		public PhookaGiveItem(final ItemStack... i) {
			items = i;
		}
		@Override
		public void accept(PlayerEntity t) {
			for(final ItemStack i : items) {
				t.dropItem(i.copy(), true);
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
		public void accept(PlayerEntity t) {
			final BlockPos origin = t.getPosition();
			BlockPos p;
			int x;
			int z;
			int attemptsLeft = 30;
			// try to teleport the player until it works, or you fail too many times
			do {
				x = min + t.getEntityWorld().getRandom().nextInt(max);
				z = min + t.getEntityWorld().getRandom().nextInt(max);
				if (t.getEntityWorld().getRandom().nextBoolean()) {
					x *= -1;
				}
				if (t.getEntityWorld().getRandom().nextBoolean()) {
					z *= -1;
				}
				p = getFloorY(t.getEntityWorld(), origin.add(x, 200, z));
			} while (attemptsLeft-- > 0 && t.attemptTeleport(p.getX() + 0.5D, p.getY(), p.getZ() + 0.5D, false));

			if (attemptsLeft > 0) {
				// assume success
				t.getEntityWorld().playSound((PlayerEntity) null, p.getX(), p.getY(), p.getZ(),
						SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				t.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}
		}

		private static BlockPos getFloorY(final World world, BlockPos p) {
			while (world.isAirBlock(p.down()) && p.getY() > 0) {
				p = p.down();
			}
			return p;
		}
	}
}
