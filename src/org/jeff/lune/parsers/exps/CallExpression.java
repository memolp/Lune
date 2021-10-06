package org.jeff.lune.parsers.exps;

import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;

/**
 * 函数调用
 * @author 覃贵锋
 *
 */
public class CallExpression extends ExpressionStatement 
{
	/** 函数名 */
	public Statement variable;
	/** 参数列表 */
	public ParamsExpression params;
	
	public CallExpression()
	{
		this.statementType = StatementType.FUNCCALL;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append(variable.toString());
		sb.append("(");
		sb.append(params.toString());
		sb.append(")");
		return sb.toString();
	}
}
