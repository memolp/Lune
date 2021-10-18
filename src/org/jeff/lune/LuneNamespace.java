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
 * 更新：
 * 1. 命名空间将在建立语法树的时候就开始生成，这样在函数身上都会有一个命名空间的对象。
 * 2. 函数内部的变量都将使用这个进行缓存和对象重复利用，减少创建对象。
 * 3. 采用缓存后，目标是降低对象的创建，提升代码运行效率。
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
	 * 添加符号变量 - 递归设置变量，存在则替换
	 * @param name
	 * @param value
	 */
	public void AddSymbol(String name, LuneObject value)
	{
		if(!this.SetSymbol(name, value))
		{
			mNamespaces.put(name, value);
		}
	}
	/**
	 * 递归更新
	 * @param name
	 * @param value
	 * @return
	 */
	private boolean SetSymbol(String name, LuneObject value)
	{
		if(mNamespaces.containsKey(name))
		{
			mNamespaces.put(name, value);
			return true;
		}else if(this.mParent != null)
		{
			return this.mParent.SetSymbol(name, value);
		}
		return false;
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
