package org.jeff.lune.parsers;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneFunction;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

public class ReturnStatement extends Statement
{
	public Statement expression;
	public ReturnStatement()
	{
		this.statementType = StatementType.RETURN;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("return ");
		sb.append(expression.toString());
		return sb.toString();
	}

	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		if(this.expression == null) return null;
		LuneObject res = this.expression.OnExecute(rt, null);
		// 如果返回的是一个函数，那么就做一个闭包
		if(res.objType == LuneObjectType.FUNCTION)
		{
			LuneFunction func = (LuneFunction)res;
			func.namespace = rt.CurrentNamespace();
		}
		return res;
	}
}
