package org.jeff.lune.parsers;

import java.util.HashMap;
import java.util.Map;

import org.jeff.lune.parsers.objs.BooleanStatement;
import org.jeff.lune.parsers.objs.IdentifierStatement;
import org.jeff.lune.parsers.objs.NumberStatement;
import org.jeff.lune.parsers.objs.StringStatement;

/**
 * 常量池 - bool将做全局的缓存
 * 字符串，变量,数字做作用域内缓存--会递归查找其存在，如果不存在则在当前的作用域创建。
 * @author 覃贵锋
 *
 */
public class StatementPool
{
	private static BooleanStatement trueStatement = new BooleanStatement(true);
	private static BooleanStatement falseStatement = new BooleanStatement(false);
	private Map<Double, NumberStatement> numCaches  = new HashMap<Double, NumberStatement>();
	private Map<String, StringStatement> stringCaches = new HashMap<String, StringStatement>();
	private Map<String, IdentifierStatement> identiesCaches = new HashMap<String, IdentifierStatement>();
	private StatementPool parent_ = null;
	
	public StatementPool()
	{
		
	}
	
	public StatementPool(StatementPool parent)
	{
		this.parent_ = parent;
	}
	
	
	/**
	 * 创建bool语句对象 - 将使用全局缓存
	 * @param v
	 * @return
	 */
	public  BooleanStatement CreateBoolStatement(boolean v)
	{
		return v ? trueStatement: falseStatement;
	}
	
	/**
	 * 获取数字语句-遍历
	 * @param v
	 * @return
	 */
	protected NumberStatement GetNumberStatement(double v)
	{
		NumberStatement state = numCaches.get(v);
		if(state == null)
		{
			if(this.parent_ != null)
				return this.parent_.GetNumberStatement(v);
		}
		return state;
	}
	
	/**
	 * 创建数字语句
	 * @param v
	 * @return
	 */
	public NumberStatement CreateNumberStatement(double v)
	{
		NumberStatement state = this.GetNumberStatement(v);
		if(state == null)
		{
			state = new NumberStatement(v);
			numCaches.put(v, state);
		}
		return state;
	}
	
	protected StringStatement GetStringStatement(String v)
	{
		StringStatement state = stringCaches.get(v);
		if(state == null)
		{
			if(this.parent_ != null)
				return this.parent_.GetStringStatement(v);
		}
		return state;
	}
	/**
	 * 创建字符串语句
	 * @param v
	 * @return
	 */
	public StringStatement CreateStringStatement(String v)
	{
		StringStatement state = this.GetStringStatement(v);
		if(state == null)
		{
			state = new StringStatement(v);
			stringCaches.put(v, state);
		}
		return state;
	}
	
	protected IdentifierStatement GetIdentifierStatement(String name)
	{
		IdentifierStatement state = identiesCaches.get(name);
		if(state == null)
		{
			if(this.parent_ != null)
				return this.parent_.GetIdentifierStatement(name);
		}
		return state;
	}
	/**
	 * 创建变量标识符语句
	 * @param name
	 * @return
	 */
	public IdentifierStatement CreateIdentifierStatement(String name)
	{
		IdentifierStatement state = this.GetIdentifierStatement(name);
		if(state == null)
		{
			state = new IdentifierStatement(name);
			identiesCaches.put(name, state);
		}
		return state;
	}
}
