package org.jeff.lune.parsers;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

public class WhileStatement extends Statement
{
	public Statement condition;
	public BlockStatement body;
	public WhileStatement()
	{
		this.statementType = StatementType.WHILE;
	}
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		LuneObject res = null;
		do
		{
			res = this.condition.OnExecute(rt, null);
			if(res != null && res.toBool())
			{
				this.body.OnExecute(rt, null);
				if(rt.IsBreakFlag || rt.IsReturnFlag)
				{
					break;
				}
				rt.IsContinueFlag = false;
			}
			else
			{
				break;
			}
		}while(true);
		rt.IsBreakFlag = false;
		rt.IsContinueFlag = false;
		return res;
	}
}
