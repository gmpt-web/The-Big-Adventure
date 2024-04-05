package fr.uge.projet.lecture;

import java.util.regex.*;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

public class Lexer {
	private static final List<Token> TOKENS = List.of(Token.values());
	private static final Pattern PATTERN = Pattern
			.compile(TOKENS.stream().map(token -> "(" + token.regex + ")").collect(Collectors.joining("|")));

	private final String text;
	private final Matcher matcher;
	private int lineNumber;

	public Lexer(String text) {
		this.text = Objects.requireNonNull(text);
		this.matcher = PATTERN.matcher(text);
		this.lineNumber = 1;
	}
	
	/**
	 * This method find next string of the matcher and his Token.
	 * @return Result containing a string and his token.
	 */
	public Result nextResult() {
		var matches = matcher.find();
		if (!matches) {
			return null;
		}
		for (var group = 1; group <= matcher.groupCount(); group++) {
			var start = matcher.start(group);
			if (start != -1) {
				var end = matcher.end(group);
				var content = text.substring(start, end);
				if (TOKENS.get(group - 1) == Token.NEWLINE) {
					lineNumber++;
				}
				return new Result(TOKENS.get(group - 1), content);
			}
		}
		throw new AssertionError();
	}


	public void reset() {
		this.matcher.reset();
		this.lineNumber = 1;
	}

	

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}