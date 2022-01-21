package org.jeff.lune.object.imp;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;

/**
 * 内置的打印方法
 * @author 覃贵锋
 *
 */
public class LunePrintFunc extends LuneExecuteable
{
	@Override
	public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
	{
		if(args.length < 1)
		{
			throw new Exception("print 方法至少需要1个参数");
		}
		
		StringBuilder sb = new StringBuilder();
		LuneObject obj;
		int size = args.length;
		for(int i =0; i < size; i++)
		{
			obj = args[i];
			sb.append(obj.strValue());
			if(i < size -1)
				sb.append(",");
		}
		System.out.println(sb.toString());
		return LuneObject.noneLuneObject;
	}

}
