package org.jeff.lune.token;

/**
 * 
 * @author 覃贵锋
 *
 */
public class Token
{
	public TokenType tokenType;
	public String tokenStr;
	public int tokenLine;
	public int tokenCol;
	/** 仅数字做个是否是浮点数的标记，后面看要不要单独区分整型和浮点数 */
	public boolean isDoubleValue = false;
	public Token(TokenType type, String text, int line, int column)
	{
		this.tokenType = type;
		this.tokenStr = text;
		this.tokenLine = line;
		this.tokenCol = column;
	}
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(this.tokenStr);
		sb.append(",");
		sb.append(String.valueOf(this.tokenLine));
		sb.append(":");
		sb.append(String.valueOf(this.tokenCol));
		sb.append("]");
		return sb.toString();
	}
}
