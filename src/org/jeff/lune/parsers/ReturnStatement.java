package org.jeff.lune.parsers;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

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
		return this.expression.OnExecute(rt, null);
	}
}
