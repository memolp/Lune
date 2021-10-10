package org.jeff.lune.parsers.exps;

import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.LuneNamespace;
import org.jeff.lune.LuneNamespaceType;
import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.parsers.objs.IdentifierStatement;

/**
 * 函数调用
 * @author 覃贵锋
 *
 */
public class CallExpression extends ExpressionStatement 
{
	/** 函数名 */
	public Statement variable;
	/** 参数列表 */
	public ParamsExpression params;
	
	public CallExpression()
	{
		this.statementType = StatementType.FUNCCALL;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append(variable.toString());
		sb.append("(");
		sb.append(params.toString());
		sb.append(")");
		return sb.toString();
	}

	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		LuneObject func = rt.GetLuneObject(variable, object);
		if(func == null) throw new RuntimeException();
		if(func.objType == LuneObjectType.EXECUTEABLE)
		{
			LuneExecuteable impfunc = (LuneExecuteable)func;
			LuneObject[] args = new LuneObject[this.params.params.size()];
			LuneObject temp_args = null;
			int index = 0;
			// 处理传参部分
			for(Statement param_state: this.params.params)
			{
				temp_args = param_state.OnExecute(rt, null);
				if(temp_args == null)
					throw new RuntimeException();
				args[index++] = temp_args;
			}
			return impfunc.Execute(rt, args);
		}else
		{
			FunctionExpression func_ = (FunctionExpression) func.GetValue();
			LuneNamespace temp_func_namespace = new LuneNamespace(LuneNamespaceType.FUNCTION, rt.CurrentNamespace());
			List<LuneObject> args = new LinkedList<LuneObject>();
			LuneObject temp_args = null;
			// 处理传参部分
			for(Statement param_state: this.params.params)
			{
				temp_args = param_state.OnExecute(rt, null);
				if(temp_args == null)
					throw new RuntimeException();
				args.add(temp_args);
			}
			temp_func_namespace.AddSymbol("arguments", args);
			int index =0;
			IdentifierStatement paramIdt = null;
			for(Statement param_state: func_.params.params)
			{
				paramIdt = (IdentifierStatement)param_state;
				if(index < args.size())
					temp_func_namespace.AddSymbol(paramIdt.name, args.get(index++));
				else
					temp_func_namespace.AddSymbol(paramIdt.name, new LuneObject());	// 传参数量可以和声明的参数不一致的。
			}
			// 将命名空间放入栈顶
			rt.PushNamespace(temp_func_namespace);
			LuneObject res = func_.OnFunctionCall(rt, null);
			// 函数执行完成后移除命名空间
			rt.PopNamespace();
			return res;
		}
	}
}
