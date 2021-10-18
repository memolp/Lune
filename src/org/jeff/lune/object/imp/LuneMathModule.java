package org.jeff.lune.object.imp;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.object.LunePropertyObject;

public class LuneMathModule extends LunePropertyObject
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
			double value = args[0].doubleValue();
			return new LuneObject(Math.floor(value));
		}
	}
	
	class _CeilFunc extends LuneExecuteable
	{

		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			double value = args[0].doubleValue();
			return new LuneObject(Math.ceil(value));
		}
	}
}
