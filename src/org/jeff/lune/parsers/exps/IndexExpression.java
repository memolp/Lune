package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneMapObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

/**
 * 下标访问表达式
 * @author 覃贵锋
 *
 */
public class IndexExpression extends ExpressionStatement
{
	/** 下标的父对象 */
	public Statement object;
	/** 下标的索引对象 */
	public Statement index;
	/**
	 * 下标访问
	 * @param line
	 * @param col
	 */
	public IndexExpression(int line, int col)
	{
		super(StatementType.INDEX, line, col);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object_) 
	{
		// 下标访问只有列表和字典支持
		LuneObject res = rt.GetLuneObject(this.object, object_);
		if(res == null) throw new RuntimeException();
		if(res.objType == LuneObjectType.LIST )
		{
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
}
