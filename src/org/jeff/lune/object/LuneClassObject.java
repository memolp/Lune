package org.jeff.lune.object;


public class LuneClassObject extends LunePropertyObject
{
	private LuneClassObject base_;
	public LuneClassObject(String className)
	{
		this.objType = LuneObjectType.CLASS;
		this.base_ = null;
		this.value_ = className;
	}
	
	public LuneClassObject(String className, LuneClassObject parent)
	{
		this.objType = LuneObjectType.CLASS;
		this.value_ = className;
		this.base_ = parent;
	}
	
	LuneClassObject(LuneClassObject cls)
	{
		this.objType = LuneObjectType.CLASS;
		this.value_ = cls.strValue();
		this.base_ = cls;
	}
	
	@Override
	public LuneObject GetAttribute(String name)
	{
		if(this.__attributes.containsKey(name))
		{
			return this.__attributes.get(name);
		}
		else if(this.base_ != null)
		{
			return this.base_.GetAttribute(name);
		}
		return LuneObject.noneLuneObject;
	}
}
