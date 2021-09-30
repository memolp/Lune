package org.jeff.lune;

import java.util.HashMap;
import java.util.Map;

/**
 * 想做件大事，看能不能搞定。
 * @author 覃贵锋
 *
 */
public class LuneContext
{
		/** 命名空间 */
		Map<String, Object> mNamespaces = new HashMap<String, Object>();
		public LuneContext()
		{
			
		}
		
		public void AddParam(String name, String value)
		{
				mNamespaces.put(name, value);
		}
		public void AddParam(String name, Object value)
		{
				mNamespaces.put(name, value);
		}
		
		public Object execute(String script)
		{
			
				return null;
		}
}
