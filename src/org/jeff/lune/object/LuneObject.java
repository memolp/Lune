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
		if(this.objType != LuneObjectType.NUMBER)
			throw new RuntimeException();
		return (long) this.value_;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(this.value_));
		return sb.toString();
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
}
