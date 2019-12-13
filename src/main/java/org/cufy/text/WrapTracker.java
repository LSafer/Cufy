/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package org.cufy.text;

import java.util.ArrayList;

/**
 * Helps to track string wrapping symbols.
 * <ul>
 *     <li>Braces: {@link open#BRACE &#123;} {@link close#BRACE &#125;}</li>
 *     <li>Brackets: {@link open#BRACKET [} {@link close#BRACKET ]}</li>
 *     <li>Chevrons: {@link open#CHEVRON ⟨} {@link close#CHEVRON ⟩}</li>
 *     <li>Corner-brackets: {@link open#CORNER_BRACKET ｢} {@link close#CORNER_BRACKET ｣}</li>
 *     <li>Double-brackets: {@link open#DOUBLE_BRACKET ⟦} {@link close#DOUBLE_BRACKET ⟧}</li>
 *     <li>Double-guillemet: {@link open#DOUBLE_GUILLEMET «} {@link close#DOUBLE_GUILLEMET »}</li>
 *     <li>Double-quote: {@link open#DOUBLE_QUOTE "} {@link close#DOUBLE_QUOTE "}</li>
 *     <li>Guillemet: {@link open#GUILLEMET <} {@link close#GUILLEMET >}</li>
 *     <li>Parenthese: {@link open#PARENTHESE (} {@link close#PARENTHESE )}</li>
 *     <li>Quote: {@link open#QUOTE '} {@link close#QUOTE '}</li>
 *     <li>Star: {@link open#STAR *} {@link close#STAR *}</li>
 *     <li>Tilde: {@link open#TILDE ~} {@link close#TILDE ~}</li>
 *     <li>Tortoise: {@link open#TORTOISE 〔} {@link close#TORTOISE 〕}</li>
 *     <li>Under-brackets: {@link open#UNDER_BRACKET ⸤} {@link close#UNDER_BRACKET ⸥}</li>
 *     <li>Underscore: {@link open#UNDER_SCORE _} {@link close#UNDER_SCORE _}</li>
 * </ul>
 *
 * @author LSaferSE
 * @version 3 release (13-Dec-2019)
 * @see <a href="https://en.wikipedia.org/wiki/Bracket">wiki.org/wiki/Bracket</a>
 * @since 21-Nov-2019
 */
public class WrapTracker {
	/**
	 * Currently appended wraps. <br/>
	 *
	 * <pre>
	 *     for example:
	 *     text passed = {a = [ b, c, 'r', "t
	 *     wraps = {, [, "
	 *     mode = "
	 * </pre>
	 */
	final protected ArrayList<Character> wraps = new ArrayList<>(50);

	/**
	 * Last wrap appended.
	 *
	 * @see #wraps
	 */
	protected char mode = ' ';

	/**
	 * Update the wraps list with the given character as part of the track.
	 *
	 * @param character to be appended
	 */
	@SuppressWarnings("OverlyComplexMethod")
	public void append(char character) {
		switch (this.mode) {
			case escape.BACK_SLASH:
				this.unwrap();
				break;
			case open.QUOTE:
			case open.DOUBLE_QUOTE:
				switch (character) {
					case escape.BACK_SLASH:
						this.wrap(escape.BACK_SLASH);
						break;
					case close.QUOTE:
					case close.DOUBLE_QUOTE:
						if (character == this.getClose(this.mode))
							this.unwrap();
						break;
				}
				break;
			case open.STAR:
			case open.TILDE:
			case open.UNDER_SCORE:
				if (character == this.getClose(this.mode))
					this.unwrap();
				break;
			default:
				switch (character) {
					case open.BRACE:
					case open.BRACKET:
					case open.CHEVRON:
					case open.CORNER_BRACKET:
					case open.DOUBLE_BRACKET:
					case open.DOUBLE_GUILLEMET:
					case open.DOUBLE_QUOTE:
					case open.GUILLEMET:
					case open.PARENTHESE:
					case open.QUOTE:
					case open.STAR:
					case open.TILDE:
					case open.TORTOISE:
					case open.UNDER_BRACKET:
					case open.UNDER_SCORE:
						this.wrap(character);
						break;
					case close.BRACE:
					case close.BRACKET:
					case close.CHEVRON:
					case close.CORNER_BRACKET:
					case close.DOUBLE_BRACKET:
					case close.DOUBLE_GUILLEMET:
//					case close.DOUBLE_QUOTE:
					case close.GUILLEMET:
					case close.PARENTHESE:
//					case close.QUOTE:
//					case close.STAR:
//					case close.TILDE:
					case close.TORTOISE:
					case close.UNDER_BRACKET:
//					case close.UNDER_SCORE:
						if (character == this.getClose(this.mode))
							this.unwrap();
						break;
				}
		}
	}

	/**
	 * Update the wraps list with the given sequence as part of the track.
	 *
	 * @param sequence to be appended
	 */
	public void append(CharSequence sequence) {
		for (Character character : sequence.toString().toCharArray())
			this.append(character);
	}

	/**
	 * Get the count of wraps appended.
	 *
	 * @return the size of wraps list
	 */
	public int length() {
		return this.wraps.size();
	}

	/**
	 * Get a close symbol from the given open symbol.
	 *
	 * @param ch to get a close symbol for
	 * @return a close symbol for the given open symbol
	 */
	@SuppressWarnings("OverlyComplexMethod")
	protected char getClose(char ch) {
		//noinspection DuplicatedCode
		switch (ch) {
			case open.BRACE:
				return close.BRACE;
			case open.BRACKET:
				return close.BRACKET;
			case open.CHEVRON:
				return close.CHEVRON;
			case open.CORNER_BRACKET:
				return close.CORNER_BRACKET;
			case open.DOUBLE_BRACKET:
				return close.DOUBLE_BRACKET;
			case open.DOUBLE_GUILLEMET:
				return close.DOUBLE_GUILLEMET;
			case open.DOUBLE_QUOTE:
				return close.DOUBLE_QUOTE;
			case open.GUILLEMET:
				return close.GUILLEMET;
			case open.PARENTHESE:
				return close.PARENTHESE;
			case open.QUOTE:
				return close.QUOTE;
			case open.STAR:
				return close.STAR;
			case open.TILDE:
				return close.TILDE;
			case open.TORTOISE:
				return close.TORTOISE;
			case open.UNDER_BRACKET:
				return close.UNDER_BRACKET;
			case open.UNDER_SCORE:
				return close.UNDER_SCORE;
			default:
				throw new IllegalArgumentException(String.valueOf(ch));
		}
	}

	/**
	 * Get an open symbol from the given close symbol.
	 *
	 * @param ch to get an open symbol for
	 * @return an open symbol for the given close symbol
	 */
	@SuppressWarnings("OverlyComplexMethod")
	protected char getOpen(char ch) {
		//noinspection DuplicatedCode
		switch (ch) {
			case close.BRACE:
				return open.BRACE;
			case close.BRACKET:
				return open.BRACKET;
			case close.CHEVRON:
				return open.CHEVRON;
			case close.CORNER_BRACKET:
				return open.CORNER_BRACKET;
			case close.DOUBLE_BRACKET:
				return open.DOUBLE_BRACKET;
			case close.DOUBLE_GUILLEMET:
				return open.DOUBLE_GUILLEMET;
			case close.DOUBLE_QUOTE:
				return open.DOUBLE_QUOTE;
			case close.GUILLEMET:
				return open.GUILLEMET;
			case close.PARENTHESE:
				return open.PARENTHESE;
			case close.QUOTE:
				return open.QUOTE;
			case close.STAR:
				return open.STAR;
			case close.TILDE:
				return open.TILDE;
			case close.TORTOISE:
				return open.TORTOISE;
			case close.UNDER_BRACKET:
				return open.UNDER_BRACKET;
			case close.UNDER_SCORE:
				return open.UNDER_SCORE;
			default:
				throw new IllegalArgumentException(String.valueOf(ch));
		}
	}

	/**
	 * Remove the last wrap (aka. mode) from wraps list.
	 */
	protected void unwrap() {
		int lastIndex = this.wraps.size() - 1;
		if (lastIndex != -1) {
			this.wraps.remove(lastIndex);
			this.mode = lastIndex == 0 ? ' ' : this.wraps.get(lastIndex - 1);
		}
	}

	/**
	 * Add the given wrap to the wraps list.
	 *
	 * @param wrap to be added
	 */
	protected void wrap(char wrap) {
		this.wraps.add(this.mode = wrap);
	}

	/**
	 * Close symbols used by this class.
	 */
	final public static class close {
		/**
		 * End braces.
		 */
		final public static char BRACE = '}';
		/**
		 * End bracket.
		 */
		final public static char BRACKET = ']';
		/**
		 * End chevron.
		 */
		final public static char CHEVRON = 10217;//'⟩'
		/**
		 * End corner brackets.
		 */
		final public static char CORNER_BRACKET = 65379;//'｣'
		/**
		 * End double brackets.
		 */
		final public static char DOUBLE_BRACKET = 10215;//'⟧'
		/**
		 * End double guillemets.
		 */
		final public static char DOUBLE_GUILLEMET = 187;//'»'
		/**
		 * Init or end double quotation marks.
		 */
		final public static char DOUBLE_QUOTE = '"';
		/**
		 * End guillemets.
		 */
		final public static char GUILLEMET = '>';
		/**
		 * End parentheses.
		 */
		final public static char PARENTHESE = ')';
		/**
		 * Init or end quotation marks.
		 */
		final public static char QUOTE = '\'';
		/**
		 * Init or end star marks.
		 */
		final public static char STAR = '*';
		/**
		 * Init or end tilde.
		 */
		final public static char TILDE = '~';
		/**
		 * End tortoise.
		 */
		final public static char TORTOISE = 12309;//'〕'
		/**
		 * End under brackets.
		 */
		final public static char UNDER_BRACKET = 11813;//'⸥'
		/**
		 * Init or end under score.
		 */
		final public static char UNDER_SCORE = '_';

		/**
		 * No instance for you!.
		 */
		private close() {
			throw new AssertionError();
		}
	}

	/**
	 * Escape symbols used by this class.
	 */
	final public static class escape {
		/**
		 * Escapes the next character.
		 */
		final public static char BACK_SLASH = '\\';

		/**
		 * No instance for you!.
		 */
		private escape() {
			throw new AssertionError();
		}
	}

	/**
	 * Open symbols used by this class.
	 */
	final public static class open {
		/**
		 * Init braces.
		 */
		final public static char BRACE = '{';
		/**
		 * Init bracket.
		 */
		final public static char BRACKET = '[';
		/**
		 * Init chevron.
		 */
		final public static char CHEVRON = 10216;//'⟨'
		/**
		 * Init corner brackets.
		 */
		final public static char CORNER_BRACKET = 65378;//'｢'
		/**
		 * Init double brackets.
		 */
		final public static char DOUBLE_BRACKET = 10214;//'⟦'
		/**
		 * Init double guillemets.
		 */
		final public static char DOUBLE_GUILLEMET = 171;//'«'
		/**
		 * Init or end double quotation marks.
		 */
		final public static char DOUBLE_QUOTE = '"';
		/**
		 * Init guillemets.
		 */
		final public static char GUILLEMET = '<';
		/**
		 * Init parentheses.
		 */
		final public static char PARENTHESE = '(';
		/**
		 * Init or end quotation marks.
		 */
		final public static char QUOTE = '\'';
		/**
		 * Init or end star marks.
		 */
		final public static char STAR = '*';
		/**
		 * Init or end tilde.
		 */
		final public static char TILDE = '~';
		/**
		 * Init tortoise.
		 */
		final public static char TORTOISE = 12308;//'〔'
		/**
		 * Init under brackets.
		 */
		final public static char UNDER_BRACKET = 11812;//'⸤'
		/**
		 * Init or end under score.
		 */
		final public static char UNDER_SCORE = '_';

		/**
		 * No instance for you!.
		 */
		private open() {
			throw new AssertionError();
		}
	}
}
