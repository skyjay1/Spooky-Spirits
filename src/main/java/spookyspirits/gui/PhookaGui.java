package spookyspirits.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import spookyspirits.entity.PhookaEntity;
import spookyspirits.init.SpookySpirits;
import spookyspirits.util.CPhookaGuiPacket;
import spookyspirits.util.PhookaRiddle;

@OnlyIn(Dist.CLIENT)
public class PhookaGui extends Screen {
	
	///////////// PHOOKA GUI /////////////
	// [ ] 3 screens
	//// [ ] both screens show:
	////// [X] background
	////// [X] text display
	////// [ ] phooka face icon
	//// [X] first screen offers to challenge riddle
	////// [X] buttons for yes and no, ESC exits GUI
	//// [X] second screen shows riddle and multiple choice answers
	//// [ ] third screen shows the result
	////// [X] hides answers
	////// [ ] either a happy or angry phooka icon
	////// [ ] triggers a noise and the phooka's despawning
	////// [ ] gives player either good effect or bad effect through packets
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
	
	protected static final int FACE_TEXTURE_X = 0;
	protected static final int FACE_TEXTURE_Y = BG_TEXTURE_Y + BG_HEIGHT + SEP;
	protected static final int FACE_WIDTH = 51;
	protected static final int FACE_HEIGHT = 47;
	protected static final int FACE_START_X = 28;
	protected static final int FACE_START_Y = 28;

	protected static final int TEXT_WIDTH = 70;
	protected static final int TEXT_HEIGHT = 85;
	protected static final int TEXT_START_X = 102;
	protected static final int TEXT_START_Y = 35;
	
	protected static final int OPTIONS_TEXTURE_X = 0;
	protected static final int OPTIONS_TEXTURE_Y = BG_TEXTURE_Y + BG_HEIGHT + SEP;
	protected static final int OPTIONS_WIDTH = 15;
	protected static final int OPTIONS_HEIGHT = 15;
	protected static final int OPTIONS_START_X = 34;
	protected static final int OPTIONS_START_Y = 132;
	
	protected static final int OPTIONS_TEXT_WIDTH = 50;
	
	private Button buttonYes;
	private Button buttonNo;
	private Button[] optionButtons;
	
	// page 0 is "accept / decline riddle"
	// page 1 is "show riddle and text entry box"
	// page 2 is "hide text entry box and show happy / angry icon, then close gui after a few seconds"
	private int page = 0;
		
	private boolean isClosing = false;
	private long ticksUntilClose = 30;
	
	private final PhookaEntity phooka;
	private final PlayerEntity player;
	private final PhookaRiddle riddle;

	protected PhookaGui(final PhookaEntity phookaIn, final PlayerEntity playerIn, final PhookaRiddle riddleIn) {
		super(new TranslationTextComponent("entity.spookyspirits.phooka.name"));
		this.page = 0; // TODO hook into config to find out if we allow opt-out
		this.phooka = phookaIn;
		this.player = playerIn;
		this.riddle = riddleIn;
	}
	
	@Override
	public void init() {
		updateBGPos();
		int w = 40;
		int h = 20;
		int x = BG_START_X + BG_WIDTH - w;
		int y = BG_HEIGHT + BG_START_Y + 8;
		this.buttonNo = this.addButton(new Button(x, y, w, h, I18n.format("gui.no"), 
				c -> /* TODO */onClose()));
		x = BG_START_X + BG_WIDTH - (w * 2) - 8;
		this.buttonYes = this.addButton(new Button(x, y, w, h, I18n.format("gui.yes"), 
				c -> setPage(1)));
		this.optionButtons = new Button[4];
		x = BG_START_X + OPTIONS_START_X;
		y = BG_START_Y + OPTIONS_START_Y;
		final String[] options = riddle.getAnswerTranslationKeys();
		optionButtons[0] = this.addButton(new PhookaGui.MultipleChoiceButton(this, 0, options[0], x, y));
		x = BG_START_X + OPTIONS_START_X + OPTIONS_WIDTH + OPTIONS_TEXT_WIDTH + SEP + 2;
		optionButtons[1] = this.addButton(new PhookaGui.MultipleChoiceButton(this, 1, options[1], x, y));
		x = BG_START_X + OPTIONS_START_X;
		y = BG_START_Y + OPTIONS_START_Y + OPTIONS_HEIGHT + SEP;
		optionButtons[2] = this.addButton(new PhookaGui.MultipleChoiceButton(this, 2, options[2], x, y));
		x = BG_START_X + OPTIONS_START_X + OPTIONS_WIDTH + OPTIONS_TEXT_WIDTH + SEP + 2;
		optionButtons[3] = this.addButton(new PhookaGui.MultipleChoiceButton(this, 3, options[3], x, y));

		
		this.updateButtons();
	}
	
	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		// draw background
		this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		updateBGPos();
		this.blitOffset = 0;
		int x = BG_START_X;
		int y = BG_START_Y;
		this.blit(x, y, BG_TEXTURE_X, BG_TEXTURE_Y, BG_WIDTH, BG_HEIGHT);
		
		// draw face background icon
		x = BG_START_X + FACE_START_X;
		y = BG_START_Y + FACE_START_Y;
		this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		//this.blit(x, y, FACE_TEXTURE_X, FACE_TEXTURE_Y, FACE_WIDTH, FACE_HEIGHT);
			// TODO draw face
		
		// draw Phooka name under icon
		final String phookaName = new TranslationTextComponent("entity.spookyspirits.phooka").applyTextStyle(TextFormatting.ITALIC).getFormattedText();
		this.font.drawString(phookaName, BG_START_X + FACE_START_X + (FACE_WIDTH - this.font.getStringWidth(phookaName)) / 2, 
				BG_START_Y + FACE_START_Y + FACE_HEIGHT + 2, 0);
		
		// draw page-specific details
		if(this.page == 0) {
			// draw riddle text in box
			drawText("phooka.offer_riddle");
		} else if(this.page == 1) {
			updateBGPos();
			// draw riddle text
			drawText(this.riddle.getRiddleTranslationKey());
		}
		
		// draw buttons, etc.
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void tick() {
		if(this.isClosing && this.ticksUntilClose-- <= 0) {
			this.onClose();
		}
	}
	
	@Override
	public void onClose() {
		//TODO this.phooka.setDespawningTicks(1);
		super.onClose();
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return this.page == 0;
	}
	
	/**
	 * Draws a string in the "text" section of the gui. The string is formatted
	 * and word-wrapped inside the box displayed by the background texture
	 * @param translationKey a key to pass to the lang handler via TranslationTextComponent
	 * @param parameters any parameters to pass with the translation key
	 **/
	private void drawText(final String translationKey, Object...parameters) {
		final String[] translated = new TranslationTextComponent(translationKey, parameters).getFormattedText().split("\\n");
		int totalLines = 0;
		for(final String line : translated) {
			totalLines += this.font.getWordWrappedHeight(line, TEXT_WIDTH);
		}
		
		// determine the scale required to fit all lines inside the box (0.5 or 1.0)
		float scale = (totalLines > TEXT_HEIGHT * 0.9F) ? 0.5F : 1.0F;
		// scale everything as needed
		GlStateManager.pushMatrix();
		GlStateManager.scalef(scale, scale, scale);
		// draw the translated text lines in the box with the appropriate scale
		int currentLine = 0;
		for(int stanzaNum = 0, numStanzas = translated.length; stanzaNum < numStanzas; stanzaNum++) {
			// get the current stanza (may take up multiple lines on its own)
			final String stanza = translated[stanzaNum];
			// determine where to start the stanza
			int startX = BG_START_X + TEXT_START_X;
			int startY = BG_START_Y + TEXT_START_Y + (int)(currentLine * this.font.FONT_HEIGHT * scale);
			// draw split (wrapped) stanza
			this.font.drawSplitString(stanza, startX, startY, (int)(TEXT_WIDTH / scale), 0);
			currentLine += this.font.getWordWrappedHeight(stanza, (int)(TEXT_WIDTH / scale)) / this.font.FONT_HEIGHT / scale;
		}
		// unscale text
		GlStateManager.popMatrix();
	}
	
	private void updateBGPos() {
		BG_START_X = (this.width - BG_WIDTH) / 2;
		BG_START_Y = 8;
	}
	
	private void updateButtons() {
		buttonYes.visible = this.page == 0;
		buttonNo.visible = this.page == 0;
		for(final Button b : this.optionButtons) {
			b.visible = this.page == 1 && !this.isClosing;
		}
	}
	
	public void setPage(int pageNum) {
		this.page = MathHelper.clamp(pageNum, 0, 2);
		this.updateButtons();
	}
	
	public void submitAnswer(final int id) {
		this.isClosing = true;
		this.minecraft.getConnection().sendPacket(new CPhookaGuiPacket(this.riddle.getName(), (byte)id));
		setPage(2);
	}
	
	class MultipleChoiceButton extends Button {
		
		private PhookaGui gui;
		private int id;
		private String translationKey;

		public MultipleChoiceButton(final PhookaGui theGui, final int idIn, final String langKey, final int x, final int y) {
			super(x, y, OPTIONS_WIDTH + OPTIONS_TEXT_WIDTH + SEP, OPTIONS_HEIGHT, "", b -> {});
			this.gui = theGui;
			this.id = idIn;
			this.translationKey = langKey;
		}
		
		@Override
		public void onClick(double mouseX, double mouseY) {
			gui.submitAnswer(id);
		}
		
		@Override
		public void render(int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				// draw icon
				final int TEX_X = this.isHovered ? OPTIONS_TEXTURE_X + OPTIONS_WIDTH + SEP : OPTIONS_TEXTURE_X;
				gui.blit(this.x, this.y, TEX_X, OPTIONS_TEXTURE_Y, OPTIONS_WIDTH, OPTIONS_HEIGHT);
				drawLabel();
			}
		}
		
		public void drawLabel() {
			// draw string
			final String localized = new TranslationTextComponent(translationKey)
					.applyTextStyle(this.isHovered ? TextFormatting.UNDERLINE : TextFormatting.RESET)
					.getFormattedText();
			gui.font.drawString(localized, this.x + OPTIONS_WIDTH + SEP, this.y + 2, 0);
		}
		
	}
}
