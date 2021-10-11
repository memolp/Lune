package org.jeff.lune.object;

import org.jeff.lune.LuneRuntime;

public class LuneTableModule extends LuneObject 
{
	public LuneTableModule()
	{
		this.objType = LuneObjectType.OBJECT;
		this.SetAttribute("len", new _TableLen());
	}
	
	class _TableLen extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) 
		{
			if(args.length != 1) throw new RuntimeException();
			if(args[0].objType == LuneObjectType.LIST)
			{
				LuneListObject list = (LuneListObject) args[0];
				int size = list.GetSize();
				return new LuneObject(size);
			}else if(args[0].objType == LuneObjectType.MAP)
			{
				LuneMapObject map = (LuneMapObject)args[0];
				int size = map.GetSize();
				return new LuneObject(size);
			}
			return null;
		}
	}
}
