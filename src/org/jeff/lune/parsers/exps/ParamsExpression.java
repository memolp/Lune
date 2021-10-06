package org.jeff.lune.parsers.exps;

import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;

public class ParamsExpression extends ExpressionStatement 
{
	public List<Statement> params = new LinkedList<Statement>();
	public ParamsExpression()
	{
		this.statementType = StatementType.PARAMS;
	}
	
	public void AddParam(Statement node)
	{
		this.params.add(node);
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		for(Statement state: params)
		{
			sb.append(state.toString());
			sb.append(",");
		}
		return sb.toString();
	}
}
