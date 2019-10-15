package spookyspirits.util;

import java.util.function.Consumer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class PhookaGiveItem implements Consumer<PlayerEntity> {
	
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
