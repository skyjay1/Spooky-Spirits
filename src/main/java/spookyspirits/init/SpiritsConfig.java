package spookyspirits.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.registries.ForgeRegistries;
import spookyspirits.effect.PhookaEffect;
import spookyspirits.entity.WispEntity;

public final class SpiritsConfig {

	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final SpiritsConfig CONFIG = new SpiritsConfig(BUILDER);
	public static final ForgeConfigSpec SPEC = BUILDER.build();
	
	private Map<String, ForgeConfigSpec.IntValue> WISP_ACTION_CHANCES = new HashMap<>();
	private ConfigValue<List<? extends String>> POTION_BLACKLIST = null;
	private final ForgeConfigSpec.BooleanValue FORCE_RIDDLES;

	public SpiritsConfig(final ForgeConfigSpec.Builder builder) {
		SpookySpirits.LOGGER.info(SpookySpirits.MODID + ": Building Config");
		builder.push("wisp");
		WispEntity.setupConfig(this, builder);
		builder.pop();
		builder.push("phooka");
		FORCE_RIDDLES = builder.comment("When true, players cannot opt out of riddles").define("force_riddles", false);
		builder.pop();
	}
	
	public void setPotionBlacklist(final List<? extends String> list) {
		if(POTION_BLACKLIST == null) {
			POTION_BLACKLIST = BUILDER.comment("Potion effects that the WispEntity should not apply")
				.defineList("potion_blacklist", list, o -> o instanceof String && 
					ForgeRegistries.POTIONS.containsKey(new ResourceLocation((String)o)));
		}
	}
	
	public void registerWispAction(final ForgeConfigSpec.Builder builder, final String name, final int defValue) {
		final ForgeConfigSpec.IntValue cfg = builder.defineInRange(name + "_chance", defValue, 0, 100);
		WISP_ACTION_CHANCES.put(name, cfg);
	}
	
	public int getActionChance(final String name) {
		return WISP_ACTION_CHANCES.containsKey(name) ? WISP_ACTION_CHANCES.get(name).get() : 0;
	}
	
	public boolean areRiddlesForced() {
		return FORCE_RIDDLES.get();
	}
	
	public boolean isEffectBlacklisted(final Effect e) {
		return POTION_BLACKLIST.get().contains(e.getRegistryName().toString());
	}
	
	public List<Effect> getGoodEffects() {
		List<? extends String> blacklist = POTION_BLACKLIST.get();
		return ForgeRegistries.POTIONS.getValues().stream()
		.filter(e -> e.isBeneficial() && !blacklist.contains(e.getRegistryName().toString()))
		.collect(Collectors.toList());
	}
	
	public List<Effect> getBadEffects() {
		List<? extends String> blacklist = POTION_BLACKLIST.get();
		return ForgeRegistries.POTIONS.getValues().stream()
		.filter(e -> !e.isBeneficial() && !blacklist.contains(e.getRegistryName().toString()))
		.collect(Collectors.toList());
	}
}
