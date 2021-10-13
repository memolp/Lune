package org.jeff.lune.object.imp;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

public class LuneMathModule extends LuneObject
{
	public LuneMathModule()
	{
		this.objType = LuneObjectType.OBJECT;
		this.SetAttribute("floor", new _FloorFunc());
		this.SetAttribute("ceil", new _CeilFunc());
	}
	
	class _FloorFunc extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			double value = args[0].toNumber();
			return new LuneObject(Math.floor(value));
		}
	}
	
	class _CeilFunc extends LuneExecuteable
	{

		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			double value = args[0].toNumber();
			return new LuneObject(Math.ceil(value));
		}
	}
}
