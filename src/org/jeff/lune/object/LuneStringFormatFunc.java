package org.jeff.lune.object;

import org.jeff.lune.LuneExecuteable;

public class LuneStringFormatFunc extends LuneObject implements LuneExecuteable
{
	public LuneStringFormatFunc()
	{
		this.objType = LuneObjectType.EXECUTEABLE;
	}
	@Override
	public LuneObject Execute(LuneObject... args)
	{
		if(args.length < 2)
			throw new RuntimeException();
		String fmt = args[0].toString();
		Object[] params = new String[args.length - 1];
		for(int i=1; i < args.length; i++)
		{
			params[i-1] = args[i].toString();
		}
		String value = String.format(fmt, params);
		return new LuneObject(value);
	}

}
