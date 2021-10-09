package org.jeff.lune.object;

import org.jeff.lune.LuneExecuteable;

public class LunePrintFunc extends LuneObject implements LuneExecuteable
{
	public LunePrintFunc()
	{
		this.objType = LuneObjectType.EXECUTEABLE;
	}
	@Override
	public LuneObject Execute(LuneObject... args)
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
