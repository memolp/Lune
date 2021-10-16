package org.jeff.lune.parsers.objs;

import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;

/**
 * 列表语句 代码中[1,2],将会继续拆分到每个
 * @author 覃贵锋
 *
 */
public class ListStatement extends Statement 
{
	/** 存放列表中的每个元素 */
	public List<Statement> elements = new LinkedList<Statement>();
	/**
	 * 列表语句结构
	 * @param line
	 * @param col
	 */
	public ListStatement(int line, int col)
	{
		super(StatementType.LIST_OBJECT, line, col);
	}
	/**
	 * 向列表中添加元素-也是单个语句
	 * @param node
	 */
	public void AddElement(Statement node)
	{
		this.elements.add(node);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 创建List对象，并将每个元素语句生成对应的元素对象
		LuneListObject list = new LuneListObject();
		for(Statement state: this.elements)
		{
			// 每个元素本身也要通过执行来生成对象
			LuneObject obj= state.OnExecute(rt, null);
			list.Append(obj);
		}
		return list;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Statement state : elements)
		{
			sb.append(state.toString());
			sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

}
