package org.jeff.lune.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LuneListObject extends LuneObject 
{
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
	
	public void Push(LuneObject v)
	{
		this.elements_.add(v);
	}
	
	public LuneObject Get(int index)
	{
		return this.elements_.get(index);
	}

	public void Set(int index, LuneObject val) 
	{
		int size = this.elements_.size();
		if(size > index)
			this.elements_.set(index, val);
		else
		{
			for(int i = size-1; i < index; i++)
			{
				this.elements_.add(val);
			}
		}
	}

	public int GetSize() 
	{
		return this.elements_.size();
	}

	private int iterIndex = 0;
	public void beginIter()
	{
		this.iterIndex = 0;
	}
	public boolean hasNext() 
	{
		return iterIndex < this.elements_.size();
	}

	public LuneObject nextElement() 
	{
		return this.elements_.get(iterIndex++);
	}
	
	public Iterator<LuneObject> iterator()
	{
		return this.elements_.iterator();
	}
}
