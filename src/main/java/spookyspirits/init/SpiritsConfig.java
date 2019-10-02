package spookyspirits.init;

import net.minecraftforge.common.ForgeConfigSpec;

public final class SpiritsConfig {

	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final SpiritsConfig CONFIG = new SpiritsConfig(BUILDER);
	public static final ForgeConfigSpec SPEC = BUILDER.build();

	public SpiritsConfig(final ForgeConfigSpec.Builder builder) {
		builder.push("config");
		builder.pop();
	}
}
