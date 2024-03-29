package tricksters.util;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.IStringSerializable;

public final class PhookaRiddle implements IStringSerializable {
	
	private final String name;
	private final String translationKey;
	private final String[] optionsTranslationKeys;
	private final byte answer;
	private final PhookaRiddle.Type difficulty;	
	private final Consumer<PlayerEntity> blessing;
	private final Consumer<PlayerEntity> cursing;
	
	private PhookaRiddle(final String id, final String riddleLang, 
			final String[] options, final int correctOption, final PhookaRiddle.Type type,
			final Consumer<PlayerEntity> answerRight, final Consumer<PlayerEntity> answerWrong) {
		this.name = id;
		this.translationKey = riddleLang;
		this.optionsTranslationKeys = options;
		this.answer = (byte)correctOption;
		this.difficulty = type;
		this.blessing = answerRight != null ? answerRight : p -> {};
		this.cursing = answerWrong != null ? answerWrong : p -> {};
		
	}

	/** @return a unique name for the riddle **/
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		String s = "name=" + name + ", answer=" + answer;
		if(optionsTranslationKeys != null && optionsTranslationKeys.length == 4) {
			s += ", option0=" + optionsTranslationKeys[0] + ", option1=" + optionsTranslationKeys[1]
					+ ", option2=" + optionsTranslationKeys[2] + ", option3=" + optionsTranslationKeys[3];
		}
		return s;
	}
	
	/** @return a list of the four options to be displayed **/
	public String[] getAnswerTranslationKeys() {
		return optionsTranslationKeys;
	}

	/** @return the translation key of the riddle **/
	public String getRiddleTranslationKey() {
		return translationKey;
	}
	
	/** @return a byte indicating which array item was the correct answer **/
	public byte getCorrectAnswer() {
		return answer;
	}
	
	/** @return the difficulty level of the riddle **/
	public PhookaRiddle.Type getDifficulty() {
		return difficulty;
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
		private final String name;
		private String translationKey = "";
		private String answerTranslationKey = "";
		private String optionTranslationKey1 = "";
		private String optionTranslationKey2 = "";
		private String optionTranslationKey3 = "";
		private PhookaRiddle.Type type = Type.EASY;
		private Consumer<PlayerEntity> blessing = null;
		private Consumer<PlayerEntity> cursing = null;
		private final Random rand = new Random();
		
		private Builder(final String id) {
			this.name = id.isEmpty() ? "null" : id;
		}
		
		/**
		 * @param id unique name for the riddle. Also used as lang key
		 * @return a new Builder instance
		 */
		public static Builder create(final String id) {
			return new Builder(id).setRiddle("phooka.riddle.".concat(id));
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
		public Builder setAnswer(final String answerKey) {
			this.answerTranslationKey = answerKey;
			return this;
		}
		
		/**
		 * Set the 3 "other" options to be listed. They will not necessarily appear
		 * in this order.
		 * @param optionKey1 a wrong answer
		 * @param optionKey2 a wrong answer
		 * @param optionKey3 a wrong answer
		 * @return instance for chaining methods
		 **/
		public Builder setOptions(final String optionKey1, final String optionKey2, final String optionKey3) {
			this.optionTranslationKey1 = optionKey1;
			this.optionTranslationKey2 = optionKey2;
			this.optionTranslationKey3 = optionKey3;
			return this;
		}
		
		/**
		 * @param reward the effect to apply upon successfully answering a riddle
		 * @return instance for chaining methods
		 **/
		public Builder addBlessing(final Consumer<PlayerEntity> reward) {
			if(this.blessing == null) {
				this.blessing = reward;
			} else {
				this.blessing = this.blessing.andThen(reward);
			}
			return this;
		}
		
		/**
		 * @param punishment the effect to apply upon failure to answer a riddle
		 * @return instance for chaining methods
		 **/
		public Builder addCurse(final Consumer<PlayerEntity> punishment) {
			if(this.cursing == null) {
				this.cursing = punishment;
			} else {
				this.cursing = this.cursing.andThen(punishment);
			}
			return this;
		}
		
		/**
		 * @param type level of difficulty: easy, medium, hard
		 * @return instance for chaining methods
		 **/
		public Builder setType(final PhookaRiddle.Type typeIn) {
			this.type = typeIn;
			return this;
		}
		
		/** @return a fully built PhookaRiddle with all attributes ready to go **/
		public PhookaRiddle build() {
			final int correctIndex = ((int)name.charAt(0)) % 4;
			final String[] answers = getArrangedString(correctIndex);
			return new PhookaRiddle(name, translationKey, answers, correctIndex, type, blessing, cursing);
		}
		
		/**
		 * @param correct the index of the correct answer
		 * @return all the options in a randomly ordered array, but the answer will
		 * always be located at the given index
		 **/
		private String[] getArrangedString(final int correct) {
			final String[] answers = new String[4];
			final List<String> others = Lists.newArrayList(optionTranslationKey1, optionTranslationKey2, optionTranslationKey3);
			for(int i = 0; i < 4; i++) {
				if(i == correct) {
					answers[i] = answerTranslationKey;
				} else if(!others.isEmpty()){
					int getIndex = rand.nextInt(others.size());
					answers[i] = others.remove(getIndex);
				}
			}
			return answers;
		}
	}
	
	public static enum Type {
		EASY, MEDIUM, HARD;
	}

}
