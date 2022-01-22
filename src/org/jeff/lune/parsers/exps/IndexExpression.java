package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneMapObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

/**
 * 下标访问表达式
 * 语法：a[xx]
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
		rt.EnterStatement(this);
		// 下标访问只有列表和字典支持
		LuneObject res = this.object.OnExecute(rt, object_);
		if(res.objType == LuneObjectType.None) 
		{
			rt.RuntimeError(this, "%s 没有找到该对象", this.object);
		}
		if(res.objType == LuneObjectType.LIST )
		{
			LuneListObject list_ = (LuneListObject)res;
			LuneObject index_ = this.index.OnExecute(rt, null);
			try
			{
				int index = (int) index_.longValue();
				res =list_.Get(index); // 下标获取
			}catch(Exception e)
			{
				rt.RuntimeError(this, "%s", e.getMessage());
			}
		}else if(res.objType == LuneObjectType.MAP)
		{
			LuneMapObject map_ = (LuneMapObject)res;
			LuneObject index_ = this.index.OnExecute(rt, null);
			res =  map_.Get(index_);
		}else
		{
			rt.RuntimeError(this, "语句:%s, 类型:%s 不支持下标操作.", res, res.objType);
		}
		rt.LeaveStatement(this);
		return res;
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
