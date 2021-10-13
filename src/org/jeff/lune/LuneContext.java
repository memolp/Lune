package org.jeff.lune;

import java.io.FileNotFoundException;

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
		public Object execfile(String filename) throws FileNotFoundException
		{
			return this.runtime_.execfile(filename);
		}
		/**
		 * 执行代码字符串
		 * @param script
		 * @return
		 */
		public Object execute(String script)
		{
			return this.runtime_.execute(script);
		}
}
