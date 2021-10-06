package org.jeff.lune.parsers;

import java.util.LinkedList;
import java.util.List;

/**
 * 语句块，存放当面快的全部语句列表
 * @author 覃贵锋
 *
 */
public class BlockStatement extends Statement
{
	/** 语句列表 */
	public List<Statement> body = new LinkedList<Statement>();
	/**
	 * 语句块
	 */
	public BlockStatement()
	{
		this.statementType = StatementType.BLOCK;
	}
	/**
	 * 添加语句
	 * @param state
	 */
	public void AddStatement(Statement state)
	{
		body.add(state);
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		for(Statement state : body)
		{
			sb.append(state.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
