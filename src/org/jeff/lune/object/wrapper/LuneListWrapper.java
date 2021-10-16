package org.jeff.lune.object.wrapper;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

/**
 * LuneListObject类的外部方法包装类，使得LuneListObject对象可以直接调用其内部的方法
 * @author 覃贵锋
 *
 */
public class LuneListWrapper extends LuneObject
{
	// 这里继承自LuneObject， 起始可以继承自LuneClass，就可以实现继承包装。但目前好像不需要这个特性。
	public LuneListWrapper()
	{
		this.objType = LuneObjectType.OBJECT;
		this.SetAttribute("size", new _wrapper_size_func());
		this.SetAttribute("append", new _wrapper_append_func());
		this.SetAttribute("remove", new _wrapper_remove_func());
		this.SetAttribute("removeAt", new _wrapper_remove_at_func());
		this.SetAttribute("insert", new _wrapper_insert_func());
		this.SetAttribute("clear", new _wrapper_clear_func());
	}
	/**
	 * LuneListObject.size() 
	 * @author 覃贵锋
	 *
	 */
	class _wrapper_size_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			LuneListObject list = (LuneListObject) args[0];
			return new LuneObject(list.Size());
		}
	}
	/**
	 * LuneListObject.append(v)
	 * @author 覃贵锋
	 *
	 */
	class _wrapper_append_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			LuneListObject list = (LuneListObject) args[0];
			list.Append(args[1]);
			return null;
		}
	}
	/**
	 * LuneListObject.remove(v)
	 * @author 覃贵锋
	 *
	 */
	class _wrapper_remove_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			LuneListObject list = (LuneListObject) args[0];
			list.Remove(args[1]);
			return null;
		}
	}
	/**
	 * LuneListObject.removeAt(index)
	 * @author 覃贵锋
	 *
	 */
	class _wrapper_remove_at_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			LuneListObject list = (LuneListObject) args[0];
			LuneObject index = args[1];
			list.RemoveAt((int) index.toLong());
			return null;
		}
	}
	/**
	 * LuneListObject.insert(index, v)
	 * @author 覃贵锋
	 *
	 */
	class _wrapper_insert_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			LuneListObject list = (LuneListObject) args[0];
			LuneObject index = args[1];
			LuneObject obj = args[2];
			list.Insert((int) index.toLong(), obj);
			return null;
		}
	}
	/**
	 * LuneListObject.clear()
	 * @author 覃贵锋
	 *
	 */
	class _wrapper_clear_func extends LuneExecuteable
	{
		@Override
		public LuneObject Execute(LuneRuntime rt, LuneObject... args)
		{
			LuneListObject list = (LuneListObject) args[0];
			list.Clear();
			return null;
		}
	}
}
