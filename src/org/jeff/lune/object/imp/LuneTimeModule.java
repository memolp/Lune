package org.jeff.lune.object.imp;

import java.util.Date;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.object.LunePropertyObject;

/**
 * 时间相关的操作模块
 * @author 覃贵锋
 *
 */
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
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
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
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
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
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
		{
			try
			{
				Thread.sleep(args[0].longValue());
			}catch (Exception e) 
			{
				throw new Exception(e.getMessage());
			}
			return LuneObject.noneLuneObject;
		}
	}
}
