package org.jeff.lune.object;

import org.jeff.lune.LuneRuntime;

public class LuneStringModule extends LuneObject
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
}
