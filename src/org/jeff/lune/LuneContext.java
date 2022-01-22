package org.jeff.lune;

import java.io.FileNotFoundException;
import java.util.List;

import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;

/**
 * 想做件大事，看能不能搞定。
 * @author 覃贵锋
 *
 */
public class LuneContext
{
		LuneRuntime runtime_ = null;
		/**
		 * 构造时创建runtime
		 */
		public LuneContext()
		{
			this.runtime_ = new LuneRuntime();
		}
		/**
		 * 添加全局符号
		 * @param name
		 * @param value
		 */
		public void AddGlobalSymbol(String name, String value)
		{
			LuneObject obj = new LuneObject(value);
			this.runtime_.mGlobalNamespaces.AddSymbol(name, obj);
		}
		public void AddGlobalSymbol(String name, double value)
		{
			LuneObject obj = new LuneObject(value);
			this.runtime_.mGlobalNamespaces.AddSymbol(name, obj);
		}
		public void AddGlobalSymbol(String name, boolean value)
		{
			LuneObject obj = new LuneObject(value);
			this.runtime_.mGlobalNamespaces.AddSymbol(name, obj);
		}
		public void AddGlobalSymbol(String name, Object value)
		{
			LuneObject obj = new LuneObject(value);
			this.runtime_.mGlobalNamespaces.AddSymbol(name, obj);
		}
		public void AddGlobalSymbol(String name, List<Object> value)
		{
			LuneListObject obj = new LuneListObject();
			this.runtime_.mGlobalNamespaces.AddSymbol(name, obj);
		}
		public void AddGlobalSymbol(String name, LuneObject obj)
		{
			this.runtime_.mGlobalNamespaces.AddSymbol(name, obj);
		}
		
		/**
		 * 一般无需调用此方法
		 * @return
		 */
		public LuneRuntime GetRuntime()
		{
			return this.runtime_;
		}
		/**
		 * 执行文件
		 * @param filename
		 * @throws FileNotFoundException
		 */
		public LuneObject execfile(String filename) throws FileNotFoundException
		{
			return this.runtime_.execfile(filename);
		}
		/**
		 * 执行代码字符串
		 * @param script
		 * @return
		 */
		public LuneObject execute(String script)
		{
			return this.runtime_.execute(script);
		}
		/**
		 * 设置运行Debug开关
		 * @param b
		 */
		public void SetDebug(boolean b)
		{
			this.runtime_.IsDebug = b;
		}
}
