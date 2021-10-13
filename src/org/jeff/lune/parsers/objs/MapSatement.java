package org.jeff.lune.parsers.objs;

import java.util.HashMap;
import java.util.Map;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneMapObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;

/**
 * 字典对象语句结构表达式
 * @author 覃贵锋
 *
 */
public class MapSatement extends Statement 
{
	/**
	 * 存储字段语句结构的key和value
	 */
	Map<Statement, Statement> elements = new HashMap<Statement, Statement>();
	/**
	 * 创建字典语句结构表达式
	 * @param line
	 * @param col
	 */
	public MapSatement(int line, int col)
	{
		super(StatementType.MAP_OBJECT, line, col);
	}
	/**
	 * 向字典中添加元素
	 * @param key
	 * @param value
	 */
	public void Add(Statement key, Statement value)
	{
		elements.put(key, value);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 获取字典语句表达式的结果，创建字典对象
		LuneMapObject map = new LuneMapObject();
		for(Statement key: this.elements.keySet())
		{
			// key 和value 都需要通过执行生成对象
			LuneObject keyObj = key.OnExecute(rt, null);
			LuneObject valueObj = this.elements.get(key).OnExecute(rt, null);
			map.Set(keyObj, valueObj);
		}
		return map;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(Statement key: elements.keySet())
		{
			sb.append(key.toString());
			sb.append(":");
			sb.append(elements.get(key).toString());
			sb.append(",");
		}
		sb.append("}");
		return sb.toString();
	}

}
