package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneFunction;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.BlockStatement;
import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.StatementType;

public class FunctionExpression extends ExpressionStatement 
{
	public String funcName;
	public BlockStatement body;
	public ParamsExpression params;
	public FunctionExpression()
	{
		this.statementType = StatementType.FUNCTION;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("function");
		sb.append("(");
		sb.append(params.toString());
		sb.append(") {\n");
		sb.append(body.toString());
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		return new LuneFunction(this);
	}
	
	public LuneObject OnFunctionCall(LuneRuntime rt, LuneObject object)
	{
		LuneObject res = this.body.OnExecute(rt, null);
		rt.IsReturnFlag = false;
		rt.IsBreakFlag = false;
		rt.IsContinueFlag = false;
		return res;
	}
}
