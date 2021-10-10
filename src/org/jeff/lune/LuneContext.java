package org.jeff.lune;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.jeff.lune.parsers.ProgramStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.SyntaxParser;

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
		
		public Object execute(String script)
		{
			
				return this.runtime_.execute(script);
		}
}
