package spookyspirits.gui;

import net.minecraft.entity.player.PlayerEntity;

public class GuiLoader {
	
	
	public static void loadPhookaGui(final PlayerEntity playerIn) {
		// only load client-side, of course
		if (!playerIn.getEntityWorld().isRemote) {
			return;
		}
		// open the gui
		//Minecraft.getInstance().displayGuiScreen(new GuiGolemBook(playerIn, itemstack));
	}
}
