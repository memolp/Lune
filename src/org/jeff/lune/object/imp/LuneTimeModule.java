package org.jeff.lune.object.imp;

import java.util.Date;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.object.LunePropertyObject;

public class LuneTimeModule extends LunePropertyObject
{
	public LuneTimeModule()
	{
		this.objType = LuneObjectType.OBJECT;
		this.SetAttribute("clock", new _clock_func());
		this.SetAttribute("localtime", new _localtime_func());
		this.SetAttribute("sleep", new _sleep_func());
	}
	/**
	 * time.clock()
	 * @author 覃贵锋
	 *
	 */
	class _clock_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			return new LuneObject(System.currentTimeMillis());
		}
	}
	/**
	 * time.localtime()
	 * @author 覃贵锋
	 *
	 */
	class _localtime_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			return new LuneObject(new Date().getTime());
		}
	}
	/**
	 * time.sleep(ms)
	 * @author 覃贵锋
	 *
	 */
	class _sleep_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			try
			{
				Thread.sleep(args[0].longValue());
			}catch (Exception e) {
			}
			return null;
		}
	}
}
