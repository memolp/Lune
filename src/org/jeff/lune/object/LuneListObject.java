package org.jeff.lune.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jeff.lune.object.wrapper.LuneListWrapper;

public class LuneListObject extends LuneObject 
{
	private static LuneListWrapper _wrapper_class = new LuneListWrapper();
	private List<LuneObject> elements_;
	public LuneListObject()
	{
		this.objType = LuneObjectType.LIST;
		this.elements_ =  new ArrayList<LuneObject>();
		this.value_  = this.elements_;
	}
	
	public LuneListObject(List<LuneObject> v)
	{
		this.objType = LuneObjectType.LIST;
		this.elements_ =  v;
		this.value_  = this.elements_;
	}
	
	/**
	 * 添加元素
	 * @param v
	 */
	public void Append(LuneObject v)
	{
		this.elements_.add(v);
	}
	/**
	 * 获取元素return list[index]
	 * @param index
	 * @return
	 */
	public LuneObject Get(int index)
	{
		return this.elements_.get(index);
	}
	/**
	 * 设置元素 list[index]=v
	 * @param index
	 * @param val
	 */
	public void Set(int index, LuneObject val) 
	{
		int size = this.elements_.size();
		if(size > index)
			this.elements_.set(index, val);
		else
		{
			throw new RuntimeException(); //index out of range
		}
	}
	/**
	 * 获取列表长度
	 * @return
	 */
	public int Size() 
	{
		return this.elements_.size();
	}
	/**
	 * 移除元素
	 * @param obj
	 */
	public void Remove(LuneObject obj)
	{
		this.elements_.remove(obj);
	}
	/**
	 * 移除指定位置的元素
	 * @param index
	 */
	public void RemoveAt(int index)
	{
		this.elements_.remove(index);
	}
	/**
	 * 插入元素
	 * @param index
	 * @param obj
	 */
	public void Insert(int index, LuneObject obj)
	{
		this.elements_.add(index, obj);
	}
	/**
	 * 清空列表
	 */
	public void Clear()
	{
		this.elements_.clear();
	}
	/**
	 * 获取列表迭代器指针
	 * @return
	 */
	public Iterator<LuneObject> iterator()
	{
		return this.elements_.iterator();
	}
	
	@Override
	public LuneObject GetAttribute(String name)
	{
		return _wrapper_class.GetAttribute(name);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(LuneObject obj : this.elements_)
		{
			sb.append(obj.toString());
			sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}
}
