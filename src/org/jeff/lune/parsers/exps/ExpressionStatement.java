package org.jeff.lune.parsers.exps;

/**
 * 表达式语句
 * 	1. 内部会有子类型，定义各种表达式
 * @author JeffXun
 *
 */
public abstract class ExpressionStatement extends Statement
{
	/** 不可创建，需继承实现 */
	public ExpressionStatement(StatementType type, int line, int col)
	{
		super(type, line, col);
	}

}
