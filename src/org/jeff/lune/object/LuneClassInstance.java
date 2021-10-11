package org.jeff.lune.object;

public class LuneClassInstance extends LuneObject
{
	private LuneClassObject class_;
	public LuneClassInstance(LuneClassObject cls)
	{
		this.objType = LuneObjectType.INSTANCE;
		this.value_ = cls;
		this.class_  = cls;
	}
	
	@Override
	public LuneObject GetAttribute(String name)
	{
		if(this.__attributes.containsKey(name))
		{
			return this.__attributes.get(name);
		}
		else if(this.class_ != null)
		{
			return this.class_.GetAttribute(name);
		}
		return null;
	}
}
