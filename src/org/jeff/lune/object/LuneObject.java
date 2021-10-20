package org.jeff.lune.object;

import java.util.HashMap;
import java.util.Map;

public class LuneObject extends Object
{
	/** 常量 true */
	public static LuneObject trueLuneObject = new LuneObject(true);
	/** 常量 false */
	public static LuneObject falseLuneObject = new LuneObject(false);
	/** 数字常量池 */
	public static Map<Double, LuneObject> constDoubleObject = new  HashMap<Double, LuneObject>();
	/** 一开始创建-2. 299的常量数字 */
	static 
	{
		for(double i = -2; i < 300; i+=1)
		{
			constDoubleObject.put(i, new LuneObject(i));
		}
	}
	/**
	 * 创建数字变量-会缓存部分
	 * @param v
	 * @return
	 */
	public static LuneObject CreateDoubleObject(double v)
	{
		LuneObject res = constDoubleObject.get(v);
		if(res == null)
		{
			res = new LuneObject(v);
		}
		return res;
	}
	/**
	 * 创建boolean变量- 直接使用缓存
	 * @param v
	 * @return
	 */
	public static LuneObject CreateBooleanObject(boolean v)
	{
		if(v) return trueLuneObject;
		return falseLuneObject;
	}
	/** 对象的类型 */
	public LuneObjectType objType = LuneObjectType.DEFAULT;
	/** 实际的对象 - LuneObject实际是对象value的外部包装 */
	protected Object value_ = null;
	/** 创建默认的LuneObject对象，value_为空值 */
	public LuneObject()
	{
		this.objType = LuneObjectType.DEFAULT;
	}
	/**
	 * 对象是数值
	 * @param v
	 */
	public LuneObject(double v)
	{
		this.value_ = v;
		//if(this.objType  != LuneObjectType.NUMBER)  TODO 类型检查
		this.objType = LuneObjectType.NUMBER;
	}
	/**
	 * 对象是布尔值
	 * @param v
	 */
	public LuneObject(boolean v)
	{
		this.value_ = v;
		this.objType = LuneObjectType.BOOL;
	}
	/**
	 * 对象是字符串
	 * @param v
	 */
	public LuneObject(String v)
	{
		this.value_ = v;
		this.objType = LuneObjectType.STRING;
	}
	/**
	 * 对象是Java的Object对象
	 * @param v
	 */
	public LuneObject(Object v)
	{
		this.value_ = v;
		this.objType = LuneObjectType.OBJECT;
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
	public double doubleValue()
	{
		if(this.objType != LuneObjectType.NUMBER)
			throw new RuntimeException();
		return (double) this.value_;
	}
	/**
	 * 转换成整数
	 * @return
	 */
	public long longValue()
	{
		return new Double(this.doubleValue()).longValue();
	}
	/**
	 * 转换成字符串
	 * @return
	 */
	public String strValue()
	{
		return String.valueOf(this.value_);
	}
	
	@Override
	public String toString() 
	{
		//if(this.objType != LuneObjectType.STRING)
		return String.valueOf(this.value_);
		/*StringBuilder sb = new StringBuilder();
		sb.append("\"");
		sb.append(this.value_);
		sb.append("\"");
		return sb.toString();*/
	}
	
	public LuneObject GetAttribute(String name)
	{
		return null;
	}
	
	public void SetAttribute(String name, LuneObject value)
	{
		
	}
	
	public boolean toBool() 
	{
		if(this.objType == LuneObjectType.NUMBER)
			return this.doubleValue()  != 0.0;
		else if(this.objType == LuneObjectType.STRING)
			return this.strValue().length() > 0;
		else if(this.objType == LuneObjectType.BOOL)
			return (boolean) this.value_;
		else if(this.objType == LuneObjectType.DEFAULT)
			return false;
		return true;
	}
	
	public boolean equals(Object obj) 
	{
		if(obj instanceof LuneObject)
		{
			LuneObject obj_ = (LuneObject)obj;
			if(this.objType != obj_.objType) return false;
			if(this.objType == LuneObjectType.NUMBER)
				return this.doubleValue() == obj_.doubleValue();
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
