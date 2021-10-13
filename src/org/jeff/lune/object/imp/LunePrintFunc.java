package org.jeff.lune.object.imp;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;

public class LunePrintFunc extends LuneExecuteable
{
	@Override
	public LuneObject Execute(LuneRuntime rt, LuneObject... args)
	{
		StringBuilder sb = new StringBuilder();
		LuneObject obj;
		int size = args.length;
		for(int i =0; i < size; i++)
		{
			obj = args[i];
			sb.append(obj.toString());
			if(i < size -1)
				sb.append(",");
		}
		System.out.println(sb.toString());
		return null;
	}

}
