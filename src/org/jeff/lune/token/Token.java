package org.jeff.lune.token;

public class Token
{
	public TokenType tokenType;
	public String tokenStr;
	public int tokenLine;
	public int tokenCol;
	public Token(TokenType type, String text, int line, int column)
	{
		this.tokenType = type;
		this.tokenStr = text;
		this.tokenLine = line;
		this.tokenCol = column;
	}
}
