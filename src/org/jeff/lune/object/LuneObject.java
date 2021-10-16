package org.jeff.lune.object;

import java.util.HashMap;

/**
 * Lune语言中数据结构的对象 -在C/C++中将使用Union进行表示
 * struct LuneObject
 * {
 * 		int type;
 * 		union{
 * 		long long iValue;
 * 	    double fValue;
 * 	    char*  sValue;
 * 		bool   bValue; 
 *     // 这些需要待实际写的时候看怎么定义
 * 	    List*  lValue;
 * 		Dict* dValue;
 * 		Function* mValue;
 * 		Class*  cValue;
 * 		} value;
 * }
 * obj (可以是数字，字符，布尔值，列表，字典，函数，类等）
 * @author 覃贵锋
 *
 */
public class LuneObject extends Object
{
	/** 对象的类型 */
	public LuneObjectType objType = LuneObjectType.DEFAULT;
	/** 
	 * 对象的属性列表 - 这里采用字段存储，Lune中对象的所有属性访问都来自此字典中，不准备支持反射，方便与C/C++保持一致。 
	 * 这里的属性对象即包括基本的数据类型，也包含列表，字段，类对象，函数等复杂对象。
	 * 
	 */
	HashMap<String, LuneObject> __attributes = new HashMap<String, LuneObject>();
	/** 实际的对象 - LuneObject实际是对象value的外部包装 */
	Object value_ = null;
	/** 创建默认的LuneObject对象，value_为空值 */
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
		if(this.objType != LuneObjectType.STRING)
			return String.valueOf(this.value_);
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		sb.append(this.value_);
		sb.append("\"");
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
