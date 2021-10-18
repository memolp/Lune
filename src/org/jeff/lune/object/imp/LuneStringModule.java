package org.jeff.lune.object.imp;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.object.LunePropertyObject;

public class LuneStringModule extends LunePropertyObject
{
	public LuneStringModule()
	{
		this.objType = LuneObjectType.OBJECT;
		this.SetAttribute("format", new LuneStringFormatFunc());
	}
	
	class LuneStringFormatFunc extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			if(args.length < 2)
				throw new RuntimeException();
			String fmt = args[0].strValue();
			Object[] params = new String[args.length - 1];
			for(int i=1; i < args.length; i++)
			{
				params[i-1] = args[i].strValue();
			}
			String value = String.format(fmt, params);
			return new LuneObject(value);
		}

	}
}
