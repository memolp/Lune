package org.jeff.lune.object;

import org.jeff.lune.LuneRuntime;

public class LunePrintFunc extends LuneExecuteable
{
	@Override
	public LuneObject Execute(LuneRuntime rt, LuneObject... args)
	{
		StringBuilder sb = new StringBuilder();
		for(LuneObject obj : args)
		{
			sb.append(obj.toString());
		}
		System.out.println(sb.toString());
		return null;
	}

}
