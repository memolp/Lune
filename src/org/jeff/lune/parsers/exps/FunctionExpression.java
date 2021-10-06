package org.jeff.lune.parsers.exps;

import org.jeff.lune.parsers.BlockStatement;
import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.StatementType;

public class FunctionExpression extends ExpressionStatement 
{
	public String funcName;
	public BlockStatement body;
	public ParamsExpression params;
	public FunctionExpression()
	{
		this.statementType = StatementType.FUNCTION;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("function");
		sb.append("(");
		sb.append(params.toString());
		sb.append(") {\n");
		sb.append(body.toString());
		sb.append("}\n");
		return sb.toString();
	}
}
