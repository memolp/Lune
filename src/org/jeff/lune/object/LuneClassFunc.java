package org.jeff.lune.object;

import org.jeff.lune.LuneRuntime;

public class LuneClassFunc extends LuneExecuteable
{

	@Override
	public LuneObject Execute(LuneRuntime rt, LuneObject... args)
	{
		if(args.length < 2)
			return new LuneClassObject(args[0].toString());
		else
		{
			LuneObject cls = args[1];
			if(cls.objType != LuneObjectType.CLASS) throw new RuntimeException();
			return new LuneClassObject(args[0].toString(), (LuneClassObject) cls);
		}
	}

}
