package org.jeff.lune.object.imp;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.object.LunePropertyObject;

/**
 * 数学相关的模块
 * 逐步往里面加吧。
 * @author 覃贵锋
 *
 */
public class LuneMathModule extends LunePropertyObject
{
	public LuneMathModule()
	{
		this.objType = LuneObjectType.OBJECT;
		this.SetAttribute("floor", new _FloorFunc());
		this.SetAttribute("ceil", new _CeilFunc());
	}
	/**
	 * 向下截取
	 * @author 覃贵锋
	 *
	 */
	class _FloorFunc extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
		{
			if(args.length < 1)
			{
				throw new Exception("math.floor 需要提供1个参数");
			}
			double value = args[0].doubleValue();
			return new LuneObject(Math.floor(value));
		}
	}
	/**
	 * 向上取整
	 * @author 覃贵锋
	 *
	 */
	class _CeilFunc extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
		{
			if(args.length < 1)
			{
				throw new Exception("math.ceil 需要提供1个参数");
			}
			double value = args[0].doubleValue();
			return new LuneObject(Math.ceil(value));
		}
	}
}
