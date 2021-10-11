package org.jeff.lune.object;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LuneMapObject extends LuneObject
{
	private Map<LuneObject, LuneObject> elements_;
	public LuneMapObject()
	{
		this.objType = LuneObjectType.MAP;
		this.elements_ = new HashMap<LuneObject, LuneObject>();
		this.value_ = this.elements_;
	}
	
	public void Set(LuneObject key, LuneObject value)
	{
		this.elements_.put(key, value);
	}
	
	public LuneObject Get(LuneObject key)
	{
		return this.elements_.get(key);
	}
	
	public int GetSize()
	{
		return this.elements_.size();
	}
	
	public Iterator<Entry<LuneObject, LuneObject>> iterator()
	{
		return this.elements_.entrySet().iterator();
	}
}
