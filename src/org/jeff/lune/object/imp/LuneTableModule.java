package org.jeff.lune.object.imp;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneMapObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

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
				int size = list.Size();
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
