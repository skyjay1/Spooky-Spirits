package spookyspirits.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import spookyspirits.init.SpookySpirits;

public final class PhookaRiddles {

	private static final Map<String, PhookaRiddle> REGISTRY = new HashMap<>();

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
	static {
		register(PhookaRiddle.Builder.create("air").setAnswer("block.minecraft.air")
				.setOptions("block.minecraft.water", "block.minecraft.oak_leaves", "item.minecraft.egg")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(p -> {
					// TODO give player speed and reduce fall damage
				}).setCurse(p -> {
					// TODO give player slowness and increase fall damage
				}).build());

		register(PhookaRiddle.Builder.create("darkness").setAnswer("darkness")
				.setOptions("", "", "")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());

		register(PhookaRiddle.Builder.create("coal").setAnswer("item.minecraft.coal")
				.setOptions("block.minecraft.fire", "block.minecraft.iron_ore", "block.minecraft.furnace")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(p -> {
					// TODO give player fortune III somehow???
				}).setCurse(p -> {
					// TODO give player effect that causes ore block drops to randomly turn to
					// coal??
				}).build());

		register(PhookaRiddle.Builder.create("egg").setAnswer("item.minecraft.egg")
				.setOptions("entity.minecraft.cow", "item.minecraft.bucket", "fish")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.EGG, 16)))
				.setCurse(p -> {
					// TODO 16 eggs are thrown at player from various directions
				}).build());

		register(PhookaRiddle.Builder.create("fire1").setAnswer("block.minecraft.fire")
				.setOptions("fish", "block.minecraft.grass", "darkness")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 20000, 4)))
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
				.setOptions("item.minecraft.seeds", "shadow", "biome.minecraft.the_void")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveEffect(new EffectInstance(Effects.SPEED, 20000, 3)))
				.setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("bed").setAnswer("block.minecraft.bed")
				.setOptions("entity.minecraft.wolf", "block.minecraft.chest", "entity.minecraft.llama")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.LIGHT_GRAY_BED)))
				.setCurse(p -> {
					// TODO
				}).build());
		
		register(PhookaRiddle.Builder.create("water").setAnswer("blocks.minecraft.water")
				.setOptions("entity.minecraft.ocelot", "entity.minecraft.horse", "block.minecraft.fire")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("hook").setAnswer("hook")
				.setOptions("item.minecraft.bread", "item.minecraft.spider_eye", "block.minecraft.cobweb")
				.setType(PhookaRiddle.Type.EASY)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("coffin").setAnswer("coffin")
				.setOptions("entity.minecraft.boat", "item.minecraft.book", "candle")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("sponge").setAnswer("block.minecraft.sponge")
				.setOptions("fish", "block.minecraft.fire", "item.minecraft.bucket")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("fire2").setAnswer("block.minecraft.fire")
				.setOptions("candle", "biome.minecraft.the_void", "entity.minecraft.husk")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("fire3").setAnswer("block.minecraft.fire")
				.setOptions("block.minecraft.grass", "entity.minecraft.zombie", "wind")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("wind").setAnswer("wind")
				.setOptions("entity.minecraft.lightning_bolt", "block.minecraft.fire", "tree")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("eye").setAnswer("eye")
				.setOptions("biome.minecraft.river", "biome.minecraft.ocean", "block.minecraft.player_head")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("horse").setAnswer("entity.minecraft.horse")
				.setOptions("entity.minecraft.boat", "entity.minecraft.rabbit", "entity.minecraft.pig")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(p -> {
					int variant = p.getEntityWorld().getRandom().nextInt(7) | 
							p.getEntityWorld().getRandom().nextInt(5) << 8;
					final HorseEntity horse = EntityType.HORSE.create(p.getEntityWorld());
					horse.setTamedBy(p);
					horse.setRearing(true);
					horse.setHorseVariant(variant);
					horse.setHorseSaddled(true);
					horse.setPosition(p.posX, p.posY, p.posZ);
					p.getEntityWorld().addEntity(horse);
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("shadow1").setAnswer("shadow")
				.setOptions("sleep", "entity.minecraft.spider", "entity.minecraft.lightning_bolt")
				.setType(PhookaRiddle.Type.MEDIUM)
				.setBlessing(p -> {
					// TODO
				}).setCurse(p -> {
					// TODO 
				}).build());
		
		register(PhookaRiddle.Builder.create("candle").setAnswer("candle")
				.setOptions("moon", "block.minecraft.fire", "item.minecraft.arrow")
				.setType(PhookaRiddle.Type.HARD)
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.TORCH, 64))).setCurse(p -> {
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
				.setBlessing(new PhookaGiveItem(new ItemStack(Items.INK_SAC, 16))).setCurse(p -> {
					// TODO a squid is stuck on your head, limiting your vision
				}).build());
	}
}
