package spookyspirits.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.IStringSerializable;

public final class PhookaRiddle implements IStringSerializable {
	
	private final String name;
	private final String translationKey;
	private final List<String> answerTranslationKey;
	private final Consumer<PlayerEntity> blessing;
	private final Consumer<PlayerEntity> cursing;
	
	private PhookaRiddle(final String id, final String riddleLang, final List<String> answerLang,
			final Consumer<PlayerEntity> answerRight, final Consumer<PlayerEntity> answerWrong) {
		this.name = id;
		this.translationKey = riddleLang;
		this.answerTranslationKey = answerLang;
		this.blessing = answerRight;
		this.cursing = answerWrong;
	}

	/** @return a unique name for the riddle **/
	@Override
	public String getName() {
		return name;
	}
	
	/** @return a list of translation keys that map to acceptable answers **/
	public List<String> getAnswerTranslationKeys() {
		return answerTranslationKey;
	}

	/** @return the translation key of the riddle **/
	public String getTranslationKey() {
		return translationKey;
	}

	/** @return a Consumer that applies this riddle's 'reward' effect **/
	public Consumer<PlayerEntity> getBlessing() {
		return blessing;
	}

	/** @return a Consumer that applies this riddle's 'punishment' effect **/
	public Consumer<PlayerEntity> getCursing() {
		return cursing;
	}

	public static class Builder {
		private String name = "";
		private String translationKey = "";
		private List<String> answerTranslationKey = new ArrayList<>();
		private Consumer<PlayerEntity> blessing = p -> p.giveExperiencePoints(10);
		private Consumer<PlayerEntity> cursing = p -> p.experienceTotal = 0;
		
		private Builder(final String id) {
			this.name = id;
		}
		
		/**
		 * @param id a unique name for the effect
		 * @return a new Builder instance
		 */
		public static Builder create(final String id) {
			return new Builder(id);
		}
		
		/**
		 * @param riddleKey the translation key that displays the riddle text
		 * @return instance for chaining methods
		 **/
		public Builder setRiddle(final String riddleKey) {
			this.translationKey = riddleKey;
			return this;
		}
		
		/**
		 * @param answerKey a translation key that maps to an acceptable answer
		 * @return instance for chaining methods
		 **/
		public Builder addAnswer(final String answerKey) {
			this.answerTranslationKey.add(answerKey);
			return this;
		}
		
		/**
		 * @param reward the effect to apply upon successfully answering a riddle
		 * @return instance for chaining methods
		 **/
		public Builder setBlessing(final Consumer<PlayerEntity> reward) {
			this.blessing = reward;
			return this;
		}
		
		/**
		 * @param punishment the effect to apply upon failure to answer a riddle
		 * @return instance for chaining methods
		 **/
		public Builder setCurse(final Consumer<PlayerEntity> punishment) {
			this.cursing = punishment;
			return this;
		}
		
		/** @return a fully built PhookaRiddle with all attributes ready to go **/
		public PhookaRiddle build() {
			return new PhookaRiddle(name, translationKey, answerTranslationKey, blessing, cursing);
		}
	}

}
