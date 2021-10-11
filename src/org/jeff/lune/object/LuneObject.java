package org.jeff.lune.object;

import java.util.HashMap;


public class LuneObject extends Object
{
	public LuneObjectType objType = LuneObjectType.DEFAULT;
	HashMap<String, LuneObject> __attributes = new HashMap<String, LuneObject>();
	
	Object value_ = null;
	public LuneObject()
	{
		this.objType = LuneObjectType.DEFAULT;
	}
	/**
	 * 对象是Java的Object对象
	 * @param v
	 */
	public LuneObject(Object v)
	{
		this.SetValue(v);
	}
	/**
	 * 对象是数值
	 * @param v
	 */
	public LuneObject(double v)
	{
		this.SetValue(v);
	}
	/**
	 * 对象是字符串
	 * @param v
	 */
	public LuneObject(String v)
	{
		this.SetValue(v);
	}
	/**
	 * 对象是布尔值
	 * @param v
	 */
	public LuneObject(boolean v)
	{
		this.SetValue(v);
	}
	/**
	 * 对象内部进行拷贝
	 * @param v
	 */
	public LuneObject(LuneObject v)
	{
		this.SetValue(v);
	}
	/**
	 * 设置对象的值为数字
	 * @param v
	 */
	public void SetValue(double v)
	{
		this.value_ = v;
		//if(this.objType  != LuneObjectType.NUMBER)  TODO 类型检查
		this.objType = LuneObjectType.NUMBER;
	}
	/**
	 * 设置对象的值为字符串
	 * @param v
	 */
	public void SetValue(String v)
	{
		this.value_ = v;
		this.objType = LuneObjectType.STRING;
	}
	public void SetValue(boolean v)
	{
		this.value_ = v;
		this.objType = LuneObjectType.BOOL;
	}
	/**
	 * 设置对象的值是一个Java对象
	 * @param v
	 */
	public void SetValue(Object v)
	{
		this.value_ = v;
		this.objType = LuneObjectType.OBJECT;
	}
	/**
	 * 设置对象的值是一个Lune对象
	 * @param v
	 */
	public void SetValue(LuneObject v)
	{
		//TODO 这里不能简单的赋值，因为如果这个对象是复杂对象需要进行一些处理
		this.value_  = v.value_;
		this.objType = v.objType;
	}
	/**
	 * 返回值
	 * @return
	 */
	public Object GetValue()
	{
		return  this.value_;
	}
	/**
	 * 转换成数字
	 * @return
	 */
	public double toNumber()
	{
		if(this.objType != LuneObjectType.NUMBER)
			throw new RuntimeException();
		return (double) this.value_;
	}
	/**
	 * 转换成整数
	 * @return
	 */
	public long toLong()
	{
		return new Double(this.toNumber()).longValue();
	}
	
	@Override
	public String toString() 
	{
		return String.valueOf(this.value_);
	}
	
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
	public LuneObject GetAttribute(String name)
	{
		return this.__attributes.get(name);
	}
	public void SetAttribute(String name, LuneObject value)
	{
		this.__attributes.put(name, value);
	}
	
	public boolean toBool() 
	{
		if(this.objType == LuneObjectType.NUMBER)
			return this.toNumber()  != 0.0;
		else if(this.objType == LuneObjectType.STRING)
			return this.toString().length() > 0;
		else if(this.objType == LuneObjectType.BOOL)
			return (boolean) this.value_;
		else if(this.objType == LuneObjectType.DEFAULT)
			return false;
		else if(this.objType == LuneObjectType.OBJECT)
			return !this.__attributes.isEmpty();
		return true;
	}
	
	public boolean equals(Object obj) 
	{
		if(obj instanceof LuneObject)
		{
			LuneObject obj_ = (LuneObject)obj;
			if(this.objType != obj_.objType) return false;
			if(this.objType == LuneObjectType.NUMBER)
				return this.toNumber() == obj_.toNumber();
			return this.value_.equals(obj_.value_);
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode()
	{
		return this.value_.hashCode();
	}
}
