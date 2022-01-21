package org.jeff.lune.object.imp.wrapper;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.object.LunePropertyObject;

/**
 * LuneListObject类的外部方法包装类，使得LuneListObject对象可以直接调用其内部的方法
 * @author 覃贵锋
 *
 */
public class LuneListWrapper extends LunePropertyObject
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
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
		{
			LuneListObject list = (LuneListObject)this.callObject;
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
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
		{
			if(args.length != 1)
			{
				rt.RuntimeError("append(element) 需要1个参数");
			}
			LuneListObject list = (LuneListObject) this.callObject;
			list.Append(args[0]);
			return args[0];
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
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
		{
			if(args.length != 1)
			{
				throw new Exception("remove(element) 需要1个参数");
			}
			LuneListObject list = (LuneListObject) this.callObject;
			// 返回true表示移除成功。
			return LuneObject.CreateBooleanObject(list.Remove(args[0]));
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
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
		{
			if(args.length != 1)
			{
				throw new Exception("removeAt(index) 需要1个参数");
			}
			LuneListObject list = (LuneListObject) this.callObject;
			LuneObject index = args[0];
			return list.RemoveAt((int) index.longValue());
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
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
		{
			if(args.length != 2)
			{
				throw new Exception("insert(index, obj) 需要2个参数");
			}
			LuneListObject list = (LuneListObject) this.callObject;
			LuneObject index = args[0];
			LuneObject obj = args[1];
			list.Insert((int) index.longValue(), obj);
			return LuneObject.noneLuneObject;
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
		public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
		{
			LuneListObject list = (LuneListObject)this.callObject;
			list.Clear();
			return LuneObject.noneLuneObject;
		}
	}
}
