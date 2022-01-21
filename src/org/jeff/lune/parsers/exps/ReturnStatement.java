package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneFunction;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

/**
 * return 语句表达式
 * @author 覃贵锋
 *
 */
public class ReturnStatement extends Statement
{
	/** return 后面是可以接表达式的，因此需要记录 */
	public Statement expression = null;
	/**
	 * return 表达式
	 * @param line
	 * @param col
	 */
	public ReturnStatement(int line, int col)
	{
		super(StatementType.RETURN, line, col);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		rt.EnterStatement(this);
		LuneObject res = LuneObject.noneLuneObject;
		if(this.expression != null)
		{
			res = this.expression.OnExecute(rt, null);
			// 如果返回的是一个函数，那么就做一个闭包
			if(res.objType == LuneObjectType.FUNCTION)
			{
				LuneFunction func = (LuneFunction)res;
				func.namespace = rt.CurrentNamespace(); // 主要是将当前的命名空间拷贝的函数身上，方便执行的时候可以索引到。
			}
		}
		rt.LeaveStatement(this);
		return res;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("return ");
		sb.append(expression.toString());
		return sb.toString();
	}

}
