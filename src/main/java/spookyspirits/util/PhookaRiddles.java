package spookyspirits.util;

public final class PhookaRiddles {
	private PhookaRiddles() {}
	
	public static final PhookaRiddle AIR = PhookaRiddle.Builder.create("air")
			.setRiddle("phooka.riddles.1").addAnswer("item.minecraft.coal").addAnswer("item.minecraft.charcoal")
			.setBlessing(p -> {
				// TODO give player fortune III somehow???
			})
			.setCurse(p -> {
				// TODO give player effect that causes ore block drops to randomly turn to coal??
			})
			.build();
	
	public static final PhookaRiddle COAL = PhookaRiddle.Builder.create("coal")
			.setRiddle("phooka.riddles.1").addAnswer("block.minecraft.air").addAnswer("phooka.answers.wind")
			.setBlessing(p -> {
				// TODO give player speed and reduce fall damage
			})
			.setCurse(p -> {
				// TODO give player slowness and increase fall damage
			})
			.build();
}
