package org.jeff.lune.object;

import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.LuneNamespace;
import org.jeff.lune.LuneRuntime;
import org.jeff.lune.parsers.exps.FunctionExpression;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.objs.IdentifierStatement;

public class LuneFunction extends LuneObject
{
	/** 只有闭包函数才需要*/
	public LuneNamespace namespace = null;

	public LuneFunction(FunctionExpression func)
	{
		this.value_ = func;
		this.objType = LuneObjectType.FUNCTION;
	}
	
	public LuneObject Exceute(LuneRuntime rt, List<Statement> params) throws Exception
	{
		// 闭包函数会有一个临时的命名空间，这里在调用这个闭包函数的时候就需要更新上去。
		if(this.namespace != null)
		{
			rt.CurrentNamespace().UpdateNamespace(this.namespace);
		}
		// 形参变实参
		List<LuneObject> args = new LinkedList<LuneObject>();
		LuneObject temp_args = null;
		// 处理传参部分
		for(Statement param_state: params)
		{
			temp_args = param_state.OnExecute(rt, null);
			// 同样这里也可以运行传null
			/*if(temp_args.objType == LuneObjectType.None)
			{
				rt.RuntimeError("%s 符号参数未找到对应的定义",  param_state);
			}*/
			args.add(temp_args);
		}
		// 如果是可变参数，那么arguments将保留全部的参数列表 - 递归就会创建N个LuneListObject。。。
		rt.CurrentNamespace().AddSymbol("arguments", new LuneListObject(args));
		// 形参部分设置赋值， 上面其实只是拿到了形参的实际引用数据，现在才逐个给函数内部的引用建立关系。
		int index =0;
		IdentifierStatement paramIdt = null;
		for(Statement param_state:  params)
		{
			paramIdt = (IdentifierStatement)param_state;
			if(index < args.size())
				rt.CurrentNamespace().AddSymbol(paramIdt.name, args.get(index++));
			else
				rt.CurrentNamespace().AddSymbol(paramIdt.name, new LuneObject());	// 传参数量可以和声明的参数不一致的。
		}
		// 准备好上面的内容后就可以执行CALL了
		FunctionExpression func = (FunctionExpression) this.value_;
		return func.OnFunctionCall(rt, null);
	}
}
