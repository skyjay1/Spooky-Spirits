package spookyspirits.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import spookyspirits.init.SpookySpirits;
import spookyspirits.util.PhookaRiddle;

public class PhookaGui extends Screen {
	
	///////////// PHOOKA GUI /////////////
	// [ ] 3 screens
	//// [ ] both screens show:
	////// [ ] background
	////// [ ] text display
	////// [ ] phooka face icon
	//// [ ] first screen offers to challenge riddle
	////// [ ] buttons for yes and no, ESC exits GUI
	//// [ ] second screen shows riddle and text box
	////// [ ] typing in text box shows text
	////// [ ] supports backspace to remove and carriage return to submit
	//// [ ] third screen shows the result
	////// [ ] either a happy or angry phooka icon
	////// [ ] triggers a noise and the phooka's despawning
	////// [ ] gives player either good effect or bad effect
	////// [ ] automatically closes gui after a few seconds
	
	protected static final ResourceLocation TEXTURE = new ResourceLocation(SpookySpirits.MODID, "textures/gui/phooka_riddle.png");

	protected static final int BG_TEXTURE_X = 0;
	protected static final int BG_TEXTURE_Y = 0;
	protected static final int BG_WIDTH = 206;
	protected static final int BG_HEIGHT = 190;
	protected int BG_START_X = 64;
	protected int BG_START_Y = 8;
	
	// icons are this many pixels apart in the texture
	protected static final int SEP = 5;
	
	protected static final int FACE_BG_TEXTURE_X = SEP;
	protected static final int FACE_BG_TEXTURE_Y = BG_TEXTURE_Y + BG_HEIGHT + SEP;
	protected static final int FACE_BG_WIDTH = 51;
	protected static final int FACE_BG_HEIGHT = 47;
	protected static final int FACE_BG_START_X = 28;
	protected static final int FACE_BG_START_Y = 28;
	
	protected static final int ENTRY_TEXTURE_X = FACE_BG_TEXTURE_X + FACE_BG_WIDTH + SEP;
	protected static final int ENTRY_TEXTURE_Y = BG_TEXTURE_Y + BG_HEIGHT + SEP;
	protected static final int ENTRY_WIDTH = 136;
	protected static final int ENTRY_HEIGHT = 35;
	protected static final int ENTRY_START_X = 35;
	protected static final int ENTRY_START_Y = 130;

	protected static final int TEXT_WIDTH = 72;
	protected static final int TEXT_HEIGHT = 85;
	protected static final int TEXT_START_X = 100;
	protected static final int TEXT_START_Y = 35;
	
	private Button buttonYes;
	private Button buttonNo;
	private Button buttonAnswer;
	
	// page 0 is "accept / decline riddle"
	// page 1 is "show riddle and text entry box"
	// page 2 is "hide text entry box and show happy / angry icon, then close gui after a few seconds"
	private int page = 0;
	
	private String answer = "";
	
	private long ticksUntilClose = 50;
	
	private final PhookaRiddle riddle;

	protected PhookaGui(final PhookaRiddle riddleIn) {
		super(new TranslationTextComponent("entity.spookyspirits.phooka.name"));
		this.page = 0; // TODO hook into config to find out if we allow opt-out
		this.riddle = riddleIn;
	}
	
	@Override
	public void init() {
		this.BG_START_X = (this.width - BG_WIDTH) / 2;
		int width = 40;
		int height = 20;
		int x = (this.width - width) / 2 - (width * 3);
		int y = BG_HEIGHT + BG_START_Y + 8;
		this.buttonYes = this.addButton(new Button(x, y, width, height, I18n.format("gui.yes"), 
				c -> /* TODO */onClose()));
		x = (this.width - width) / 2 + (width * 2);
		this.buttonNo = this.addButton(new Button(x, y, width, height, I18n.format("gui.no"), 
				c -> /* TODO */onClose()));
		width = 90;
		x = (this.width - width) / 2;
		this.buttonAnswer = this.addButton(new Button(x, y, width, height, I18n.format("gui.give_answer"), 
				c -> /* TODO */onClose()));
		
		this.updateButtons();
	}
	
	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		// draw background
		this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		BG_START_X = (this.width - BG_WIDTH) / 2;
		this.blit(BG_START_X, BG_START_Y, BG_TEXTURE_X, BG_TEXTURE_Y, BG_WIDTH, BG_HEIGHT);
		// draw face background icon
		this.blit(BG_START_X + FACE_BG_START_X, BG_START_Y + FACE_BG_START_Y, FACE_BG_TEXTURE_X, FACE_BG_TEXTURE_Y, FACE_BG_WIDTH, FACE_BG_HEIGHT);
			// TODO
		// draw Phooka name under icon
			// TODO
		if(this.page == 0) {
			// draw text in box
			drawText("phooka.gui.offer_riddle");
		} else if(this.page == 1) {
			// draw the chosen riddle
			drawText(this.riddle.getTranslationKey());
			// draw Text Entry box
			this.blit(BG_START_X + ENTRY_START_X, BG_START_Y + ENTRY_START_Y, ENTRY_TEXTURE_X, ENTRY_TEXTURE_Y, ENTRY_WIDTH, ENTRY_HEIGHT);
			// TODO draw current text
			this.font.drawString(answer, BG_START_X + ENTRY_START_X + 8, BG_START_Y + ENTRY_START_Y + 8, 0);
		}
		
		// draw buttons, etc.
		super.render(mouseX, mouseY, partialTicks);
	}
	
	private void drawText(final String translationKey, Object...parameters) {
		final String translated = I18n.format(translationKey, parameters);
		this.font.drawSplitString(translated, TEXT_START_X + BG_START_X, TEXT_START_Y + BG_START_Y, TEXT_WIDTH, 0);
		// TODO resize as needed in order to fit
	}
	
	private void updateButtons() {
		buttonYes.visible = this.page == 0;
		buttonNo.visible = this.page == 0;
		buttonAnswer.visible = this.page == 1;
	}
	
	@Override
	public void removed() {
		// TODO
	}

}
