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
		rt.EnterStatement(this);
		// 先计算右侧值
		LuneObject res = this.variable.OnExecute(rt, null);
		try
		{
			switch(this.opType)
			{
				case OP_NOT: // true or false
					if(res.toBool())
						res = LuneObject.falseLuneObject;
					else
						res =  LuneObject.trueLuneObject;
					break;
				case OP_BIT_NOT: // 取反
					res = new LuneObject(~res.longValue());
					break;
				default:
					rt.RuntimeError(this, "%s 语句错误", this.variable);
			}
		}catch(Exception e)
		{
			rt.RuntimeError(this, "%s", e.getMessage());
		}
		rt.LeaveStatement(this);
		return res;
	}
}
