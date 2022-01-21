package org.jeff.lune.object.imp;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;

/**
 * for i in range(11) 用于做序号迭代。
 * @author 覃贵锋
 *
 */
public class LuneRangeFunc extends LuneExecuteable 
{

	@Override
	public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
	{
		long start = 0;
		long end = 0;
		long step = 1;
		if(args.length == 1)
			end = args[0].longValue();
		if(args.length == 2)
		{
			start = args[0].longValue();
			end = args[1].longValue();
		}
		if(args.length == 3)
		{
			start = args[0].longValue();
			end = args[1].longValue();
			step = args[2].longValue();
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
