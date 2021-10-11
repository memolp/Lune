package org.jeff.lune.parsers.objs;

import java.util.HashMap;
import java.util.Map;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneMapObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;

public class MapSatement extends Statement 
{
	Map<Statement, Statement> elements = new HashMap<Statement, Statement>();
	public MapSatement()
	{
		this.statementType = StatementType.MAP_OBJECT;
	}
	public void Add(Statement key, Statement value)
	{
		elements.put(key, value);
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
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		LuneMapObject map = new LuneMapObject();
		for(Statement key: this.elements.keySet())
		{
			LuneObject keyObj = key.OnExecute(rt, null);
			LuneObject valueObj = this.elements.get(key).OnExecute(rt, null);
			map.Set(keyObj, valueObj);
		}
		return map;
	}
}
