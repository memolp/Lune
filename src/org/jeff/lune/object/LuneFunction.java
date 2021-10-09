package org.jeff.lune.object;

import org.jeff.lune.parsers.exps.FunctionExpression;

public class LuneFunction extends LuneObject
{
	public LuneFunction(FunctionExpression func)
	{
		this.value_ = func;
		this.objType = LuneObjectType.FUNCTION;
	}
}
