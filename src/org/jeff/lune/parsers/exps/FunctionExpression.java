package org.jeff.lune.parsers.exps;

import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneFunction;
import org.jeff.lune.object.LuneObject;

/**
 * 函数声明表达式
 * @author 覃贵锋
 *
 */
public class FunctionExpression extends ExpressionStatement 
{
	/** 函数的内部体 */
	public BlockStatement body;
	/** 函数声明的形参列表 */
	public List<Statement> params;
	/** 函数语句对应的函数对象 */
	LuneFunction _thisFunc;
	/**
	 * 函数声明
	 * @param line
	 * @param col
	 */
	public FunctionExpression(int line, int col)
	{
		super(StatementType.FUNCTION, line, col);
		this.params = new LinkedList<Statement>();
		_thisFunc = new LuneFunction(this); // 将函数包装一下
	}
	/**
	 * 这里执行只是返回LuneFunction对象，然后再调用里面的执行
	 */
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 返回已经生产好的函数对象
		return _thisFunc;
	}
	/**
	 * 这个才是函数调用的时候执行的真正逻辑
	 * @param rt
	 * @param object
	 * @return
	 */
	public LuneObject OnFunctionCall(LuneRuntime rt, LuneObject object)
	{
		// 执行函数内部的语句
		LuneObject res = this.body.OnExecute(rt, null);
		// 重置全部打断标记
		rt.IsReturnFlag = false;
		rt.IsBreakFlag = false;
		rt.IsContinueFlag = false;
		return res;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("function");
		sb.append("(");
		sb.append(params.toString());
		sb.append(") {\n");
		sb.append(body.toString());
		sb.append("}\n");
		return sb.toString();
	}

}
