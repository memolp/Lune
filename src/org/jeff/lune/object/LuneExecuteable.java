package org.jeff.lune.object;

import org.jeff.lune.LuneRuntime;

public abstract class LuneExecuteable extends LuneObject
{
	public LuneExecuteable()
	{
		this.objType = LuneObjectType.EXECUTEABLE;
	}
	public abstract LuneObject Execute(LuneRuntime rt, LuneObject ...args);
}
