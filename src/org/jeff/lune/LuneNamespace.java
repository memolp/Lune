package org.jeff.lune;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jeff.lune.object.LuneObject;

/**
 * 命名空间
 * 1. 全局命名空间
 * 2. 代码块命名空间等
 * @author 覃贵锋
 *
 */
public class LuneNamespace
{
	LuneNamespaceType mNameType;
	Map<String, LuneObject> mNamespaces = new HashMap<String, LuneObject>();
	LuneNamespace mParent = null;
	public LuneNamespace(LuneNamespaceType type)
	{
		this.mNameType = type;
	}
	/**
	 * 创建命名空间，并指定父对象，允许递归查找
	 * @param parent
	 */
	public LuneNamespace(LuneNamespaceType type, LuneNamespace parent)
	{
		this.mNameType = type;
		this.mParent = parent;
	}
	/**
	 * 添加符号变量
	 * @param name
	 * @param value
	 */
	public void AddSymbol(String name, String value)
	{
		mNamespaces.put(name, new LuneObject(value));
	}
	/**
	 * 添加符号变量
	 * @param name
	 * @param value
	 */
	public void AddSymbol(String name, Object value)
	{
		mNamespaces.put(name, new LuneObject(value));
	}
	/**
	 * 添加符号变量
	 * @param name
	 * @param value
	 */
	public void AddSymbol(String name, LuneObject value)
	{
		mNamespaces.put(name, value);
	}
	/**
	 * 将另一个的命名空间拷贝
	 * @param n
	 */
	public void UpdateNamespace(LuneNamespace n)
	{
		Iterator<Entry<String, LuneObject>> iter = n.mNamespaces.entrySet().iterator();
		Entry<String, LuneObject> val;
		while(iter.hasNext())
		{
			val = iter.next();
			this.AddSymbol(val.getKey(), val.getValue());
		}
	}
	/**
	 * 获取符号对象
	 * @param name
	 * @return
	 */
	public LuneObject GetSymbol(String name)
	{
		if(mNamespaces.containsKey(name))
		{
			return mNamespaces.get(name);
		}else
		{
			if(this.mParent != null)
			{
				return this.mParent.GetSymbol(name);
			}
		}
		return null;
	}
}
