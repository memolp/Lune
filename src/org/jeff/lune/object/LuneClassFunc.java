package org.jeff.lune.object;

import org.jeff.lune.LuneRuntime;

/**
 * class 声明类
 * @author 覃贵锋
 *
 */
public class LuneClassFunc extends LuneExecuteable
{

	@Override
	public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
	{
		if(args.length < 2)
		{
			if(args[0].objType != LuneObjectType.STRING)  // 声明类，第一个参数是字符串用于标记类名。
			{
				throw new Exception(String.format("声明类的第一个参数必须是字符串，而给的是%s类型", args[0].objType));
			}
			return new LuneClassObject(args[0].strValue());
		}
		else
		{
			if(args[0].objType != LuneObjectType.STRING)  // 声明类，第一个参数是字符串用于标记类名。
			{
				throw new Exception(String.format("声明类的第一个参数必须是字符串，而给的是%s类型", args[0].objType));
			}
			LuneObject cls = args[1];
			if(cls.objType != LuneObjectType.CLASS)   // 如果是继承那么，第二个参数必须是类
			{
				throw new Exception(String.format("继承自%s ，必须提供类类型，而不是%s类型", cls, cls.objType));
			}
			return new LuneClassObject(args[0].strValue(), (LuneClassObject) cls);
		}
	}

}
