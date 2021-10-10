package org.jeff.lune.parsers;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

public class BreakStatement extends Statement 
{
	public BreakStatement()
	{
		this.statementType = StatementType.BREAK;
	}
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		return null;
	}

}
