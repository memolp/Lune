package org.jeff.lune.parsers;

public class ReturnStatement extends Statement
{
	public Statement expression;
	public ReturnStatement()
	{
		this.statementType = StatementType.RETURN;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("return ");
		sb.append(expression.toString());
		return sb.toString();
	}
}
