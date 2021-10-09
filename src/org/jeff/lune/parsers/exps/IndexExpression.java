package org.jeff.lune.parsers.exps;

import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;

public class IndexExpression extends ExpressionStatement
{
	public Statement object;
	public Statement index;
	public IndexExpression()
	{
		this.statementType = StatementType.INDEX;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(object.toString());
		sb.append("[");
		sb.append(index.toString());
		sb.append("]");
		return sb.toString();
	}
}
