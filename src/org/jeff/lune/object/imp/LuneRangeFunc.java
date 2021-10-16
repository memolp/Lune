package org.jeff.lune.object.imp;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;

public class LuneRangeFunc extends LuneExecuteable 
{

	@Override
	public LuneObject Execute(LuneRuntime rt, LuneObject... args) 
	{
		long start = 0;
		long end = 0;
		long step = 1;
		if(args.length == 1)
			end = args[0].toLong();
		if(args.length == 2)
		{
			start = args[0].toLong();
			end = args[1].toLong();
		}
		if(args.length == 3)
		{
			start = args[0].toLong();
			end = args[1].toLong();
			step = args[2].toLong();
		}
		LuneListObject range = new LuneListObject();
		if(start < end)
		{
			for(long i = start; i < end;  i += step)
			{
				range.Append(new LuneObject(i));
			}
		}else
		{
			for(long i = start; i > end;  i += step)
			{
				range.Append(new LuneObject(i));
			}
		}
		return range;
	}

}
