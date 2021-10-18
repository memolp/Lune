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
public class LunePropertyObject extends LuneObject
{
	/** 
	 * 对象的属性列表 - 这里采用字段存储，Lune中对象的所有属性访问都来自此字典中，不准备支持反射，方便与C/C++保持一致。 
	 * 这里的属性对象即包括基本的数据类型，也包含列表，字段，类对象，函数等复杂对象。
	 */
	HashMap<String, LuneObject> __attributes = new HashMap<String, LuneObject>();
	
	/** 创建默认的LuneObject对象，value_为空值 */
	public LunePropertyObject()
	{
		this.objType = LuneObjectType.DEFAULT;
	}
	/**
	 * 对象内部进行拷贝
	 * @param v
	 */
	public LunePropertyObject(LuneObject v)
	{
		this.SetValue(v);
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
	
	@Override
	public boolean toBool()
	{
		if(this.objType == LuneObjectType.OBJECT)
			return !this.__attributes.isEmpty();
		return super.toBool();
	}
	
	public LuneObject GetAttribute(String name)
	{
		return this.__attributes.get(name);
	}
	public void SetAttribute(String name, LuneObject value)
	{
		this.__attributes.put(name, value);
	}
}
