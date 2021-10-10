package org.jeff.lune.parsers;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

public class ContinueStatement extends Statement 
{
	public ContinueStatement()
	{
		this.statementType = StatementType.CONTINUE;
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		return null;
	}

}
