package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.token.TokenType;

/**
 * 一元表达式
 * @author 覃贵锋
 *
 */
public class UnaryExpression extends ExpressionStatement 
{
	public TokenType  opType;
	public Statement variable;
	
	/**
	 * 一元表达式
	 * @param type
	 * @param node
	 * @param line
	 * @param col
	 */
	public UnaryExpression(TokenType type, Statement node, int line, int col)
	{
		super(StatementType.EXPRESSION_UNARY, line, col);
		this.opType = type;
		this.variable = node;
	}
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 先计算右侧值
		LuneObject res = this.variable.OnExecute(rt, null);
		switch(this.opType)
		{
			case OP_NOT:
				res.SetValue(! res.toBool());
				return res;
			case OP_BIT_NOT:
				res.SetValue(~res.toLong());
				return res;
			default:
				throw new RuntimeException();
		}
	}
}
