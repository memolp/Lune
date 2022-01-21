package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

/**
 * 属性访问表达式
 * @author 覃贵锋
 *
 */
public class MemberExpression extends ExpressionStatement
{
	/** 父对象 */
	public Statement parent;
	/** 子对象 */
	public Statement child;
	
	/**
	 * 属性访问
	 * @param line
	 * @param col
	 */
	public MemberExpression(int line, int col)
	{
		super(StatementType.MEMBER, line, col);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		rt.EnterStatement(this);
		// 先获取父对象
		LuneObject obj = rt.GetLuneObject(parent, object);
		if(obj.objType == LuneObjectType.None) 
		{
			rt.RuntimeError(this, "%s 符号未找到.",  parent);
		}
		// 再执行子对象表达式-并将父对象传入
		LuneObject res =  this.child.OnExecute(rt, obj);
		rt.LeaveStatement(this);
		return res;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(parent.toString());
		sb.append("]");
		sb.append(".");
		sb.append(child.toString());
		return sb.toString();
	}

}
