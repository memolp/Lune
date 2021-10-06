package org.jeff.lune.parsers.exps;

import java.util.HashMap;
import java.util.Map;

import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;

public class MapExpression extends ExpressionStatement 
{
	Map<Statement, Statement> elements = new HashMap<Statement, Statement>();
	public MapExpression()
	{
		this.statementType = StatementType.MAP_OBJECT;
	}
	public void Add(Statement key, Statement value)
	{
		elements.put(key, value);
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(Statement key: elements.keySet())
		{
			sb.append(key.toString());
			sb.append(":");
			sb.append(elements.get(key).toString());
			sb.append(",");
		}
		sb.append("}");
		return sb.toString();
	}
}
