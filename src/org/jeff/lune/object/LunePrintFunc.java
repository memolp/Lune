package org.jeff.lune.object;

import org.jeff.lune.LuneRuntime;

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
