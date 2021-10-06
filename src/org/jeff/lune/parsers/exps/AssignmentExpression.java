package org.jeff.lune.parsers.exps;

import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;

/**
 * 赋值表达式
 * @author 覃贵锋
 *
 */
public class AssignmentExpression extends ExpressionStatement 
{
	/** 左边变量 */
	public Statement variable;
	/** 右边值 */
	public Statement value;
	/**
	 * 赋值表达式
	 */
	public AssignmentExpression()
	{
		this.statementType = StatementType.ASSIGNMENT;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append(variable.toString());
		sb.append(" = ");
		sb.append(value.toString());
		return sb.toString();
	}
}
