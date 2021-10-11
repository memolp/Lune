package org.jeff.lune.object;

import org.jeff.lune.LuneNamespace;
import org.jeff.lune.parsers.exps.FunctionExpression;

public class LuneFunction extends LuneObject
{
	/** 只有闭包函数才需要*/
	public LuneNamespace namespace = null;

	public LuneFunction(FunctionExpression func)
	{
		this.value_ = func;
		this.objType = LuneObjectType.FUNCTION;
	}
}
