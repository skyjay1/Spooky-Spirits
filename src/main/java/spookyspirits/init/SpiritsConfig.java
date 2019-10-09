package spookyspirits.init;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.registries.ForgeRegistries;
import spookyspirits.entity.WispEntity;

public final class SpiritsConfig {

	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final SpiritsConfig CONFIG = new SpiritsConfig(BUILDER);
	public static final ForgeConfigSpec SPEC = BUILDER.build();
	
	public final Map<String, ForgeConfigSpec.IntValue> WISP_ACTION_CHANCES = new HashMap<>();
	private final ConfigValue<List<? extends String>> POTION_BLACKLIST;

	public SpiritsConfig(final ForgeConfigSpec.Builder builder) {
		SpookySpirits.LOGGER.info(SpookySpirits.MODID + ": Building Config");
		POTION_BLACKLIST = WispEntity.setupConfig(this, builder);
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
