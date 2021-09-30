package org.jeff.lune.object;

import java.util.HashMap;

public class LuneObject extends Object
{
	HashMap<String, LuneObject> __attributes = new HashMap<String, LuneObject>();
	
	public LuneObject clone()
	{
		LuneObject obj = new LuneObject();
		LuneObject temp = null;
		for(String key : __attributes.keySet())
		{
			temp = __attributes.get(key);
			obj.__attributes.put(key, temp.clone());
		}
		return obj;
	}
}
