package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneMapObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;

public class IndexExpression extends ExpressionStatement
{
	public Statement object;
	public Statement index;
	public IndexExpression()
	{
		this.statementType = StatementType.INDEX;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(object.toString());
		sb.append("[");
		sb.append(index.toString());
		sb.append("]");
		return sb.toString();
	}

	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object_) 
	{
		LuneObject res = rt.GetLuneObject(this.object, object_);
		if(res == null) throw new RuntimeException();
		if(res.objType == LuneObjectType.LIST )
		{
			if(this.index.statementType != StatementType.NUMBER) throw new RuntimeException();
			LuneListObject list_ = (LuneListObject)res;
			LuneObject index_ = this.index.OnExecute(rt, null);
			return list_.Get((int) index_.toLong());
		}else if(res.objType == LuneObjectType.MAP)
		{
			LuneMapObject map_ = (LuneMapObject)res;
			LuneObject index_ = this.index.OnExecute(rt, null);
			return map_.Get(index_);
		}else
		{
			throw new RuntimeException();
		}
	}
}
