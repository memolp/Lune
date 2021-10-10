package org.jeff.lune.parsers.exps;

import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;

public class ListExpression extends ExpressionStatement 
{
	public List<Statement> elements = new LinkedList<Statement>();
	public ListExpression()
	{
		this.statementType = StatementType.LIST_OBJECT;
	}
	/**
	 * 添加元素
	 * @param node
	 */
	public void AddElement(Statement node)
	{
		this.elements.add(node);
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Statement state : elements)
		{
			sb.append(state.toString());
			sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		LuneListObject list = new LuneListObject();
		for(Statement state: this.elements)
		{
			LuneObject obj= state.OnExecute(rt, null);
			list.Push(obj);
		}
		return list;
	}
}
