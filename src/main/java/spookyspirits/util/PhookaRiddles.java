package spookyspirits.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import spookyspirits.init.SpookySpirits;

public final class PhookaRiddles {
	
	private static final Map<String, PhookaRiddle> REGISTRY = new HashMap<>();
	
	private PhookaRiddles() {}
	
	@Nullable
	public static PhookaRiddle getByName(final String id) {
		return REGISTRY.containsKey(id) ? REGISTRY.get(id) : null;
	}
	
	public static PhookaRiddle register(final PhookaRiddle riddle) {
		if(REGISTRY.containsKey(riddle.getName())) {
			SpookySpirits.LOGGER.error("Tried to add duplicate PhookaRiddle with key '" + riddle.getName() + "' - Skipping");
			return riddle;
		}
		return REGISTRY.put(riddle.getName(), riddle);
	}
	
	public static void registerAll(final PhookaRiddle... riddles) {
		for(final PhookaRiddle r : riddles) {
			register(r);
		}
	}
	
	//// REGISTER ALL PHOOKA RIDDLES ////
	static {
		// AIR
		register(PhookaRiddle.Builder.create("air")
				.setRiddle("phooka.gui.riddle1").addAnswer("block.minecraft.air").addAnswer("phooka.gui.answer.wind")
				.setBlessing(p -> {
					// TODO give player speed and reduce fall damage
				})
				.setCurse(p -> {
					// TODO give player slowness and increase fall damage
				})
				.build());
		// COAL
		register(PhookaRiddle.Builder.create("coal")
				.setRiddle("phooka.gui.riddle1").addAnswer("item.minecraft.coal").addAnswer("item.minecraft.charcoal")
				.setBlessing(p -> {
					// TODO give player fortune III somehow???
				})
				.setCurse(p -> {
					// TODO give player effect that causes ore block drops to randomly turn to coal??
				})
				.build());
		
	}
}
