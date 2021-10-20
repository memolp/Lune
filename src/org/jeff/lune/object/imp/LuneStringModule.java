package org.jeff.lune.object.imp;

import java.util.Iterator;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.object.LunePropertyObject;

public class LuneStringModule extends LunePropertyObject
{
	public LuneStringModule()
	{
		this.objType = LuneObjectType.OBJECT;
		this.SetAttribute("format", new _format_func());
		this.SetAttribute("concat", new _concat_func());
	}
	
	/**
	 * string.format()
	 * @author 覃贵锋
	 *
	 */
	class _format_func extends LuneExecuteable
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
				params[i-1] = args[i].GetValue();
			}
			String value = String.format(fmt, params);
			return new LuneObject(value);
		}
	}
	/**
	 * string.concat(list or dict, tag)
	 * @author 覃贵锋
	 *
	 */
	class _concat_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			if(args.length != 2)
				throw new RuntimeException();
			LuneObject list_obj = args[0];
			LuneObject tag = args[1];
			StringBuilder sb = new StringBuilder();
			if(list_obj.objType == LuneObjectType.LIST)
			{
				LuneListObject list_ = (LuneListObject)list_obj;
				Iterator<LuneObject> iter = list_.iterator();
				int size = list_.Size();
				int i = 0;
				while(iter.hasNext())
				{
					sb.append(iter.next());
					if(i + 1 < size)
					{
						sb.append(tag);
					}
					i++;
				}
				return new LuneObject(sb.toString());
			}else if(list_obj.objType == LuneObjectType.MAP)
			{
				// 待完成
				/*LuneMapObject map_ = (LuneMapObject)list_obj;
				Iterator<Entry<LuneObject, LuneObject>> iter = map_.iterator();
				while(iter.hasNext())
				{
					sb.append(iter.next().getKey());
				}
				return new LuneObject(sb.toString());*/
				throw new RuntimeException();
			}
			return null;
		}
		
	}
}
