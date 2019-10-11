package spookyspirits.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import spookyspirits.entity.PhookaEntity;
import spookyspirits.util.PhookaRiddle;

public class GuiLoader {
	
	
	public static void loadPhookaGui(final PhookaEntity phooka, final PlayerEntity playerIn, final PhookaRiddle riddle) {
		// only load client-side, of course
		if (!playerIn.getEntityWorld().isRemote) {
			return;
		}
		// open the gui
		Minecraft.getInstance().displayGuiScreen(new PhookaGui(phooka, playerIn, riddle));
	}
}
