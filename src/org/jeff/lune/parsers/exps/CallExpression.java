package org.jeff.lune.parsers.exps;

import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.LuneNamespace;
import org.jeff.lune.LuneNamespaceType;
import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneClassInstance;
import org.jeff.lune.object.LuneClassObject;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneFunction;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
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
	public List<Statement> params;
	/**
	 * 函数调用
	 * @param line
	 * @param col
	 */
	public CallExpression(int line, int col)
	{
		super(StatementType.FUNCCALL, line, col);
		this.params = new LinkedList<Statement>();
	}

	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 函数调用分为 三类 Java导出的可执行方法， 类执行，其他函数执行
		// 获取函数名对象
		LuneObject func = rt.GetLuneObject(variable, object);
		if(func == null) 
		{
			throw new RuntimeException();
		}
		// Java 执行方法
		if(func.objType == LuneObjectType.EXECUTEABLE)
		{
			int size = this.params.size() + ((object != null && object.objType == LuneObjectType.INSTANCE) ? 1: 0);
			// 先处理传参部分，将参数全部生成对象
			LuneObject[] args = new LuneObject[size];
			LuneObject temp_args = null;
			int index = 0;
			if(object != null && object.objType == LuneObjectType.INSTANCE)
			{
				args[index ++] = object;
			}
			// 处理传参部分
			for(Statement param_state: this.params)
			{
				temp_args = param_state.OnExecute(rt, null);
				if(temp_args == null)
				{
					throw new RuntimeException();
				}
				args[index ++] = temp_args;
			}
			LuneExecuteable impfunc = (LuneExecuteable)func;
			return impfunc.Execute(rt, args);
		}else
		{
			// 先处理传参部分，将参数全部生成对象
			List<LuneObject> args = new LinkedList<LuneObject>();
			LuneObject temp_args = null;
			// 处理传参部分
			for(Statement param_state: this.params)
			{
				temp_args = param_state.OnExecute(rt, null);
				if(temp_args == null)
				{
					throw new RuntimeException();
				}
				args.add(temp_args);
			}
			// 对类执行调用，相当于创建对象
			if(func.objType == LuneObjectType.CLASS)
			{
				LuneClassInstance obj = new LuneClassInstance((LuneClassObject) func);
				// 这里在创建对象后，立即执行其构造函数
				LuneObject ctor_ = obj.GetAttribute("ctor");
				if(ctor_ != null && ctor_.objType == LuneObjectType.FUNCTION)
				{
					FunctionExpression func_ = (FunctionExpression) ctor_.GetValue();
					// 创建临时命名空间
					LuneNamespace temp_func_namespace = new LuneNamespace(LuneNamespaceType.FUNCTION, rt.CurrentNamespace());
					// 将传参设置给系统变量，方便可变参数使用
					temp_func_namespace.AddSymbol("arguments", new LuneListObject(args));
					// this对象赋值
					temp_func_namespace.AddSymbol("this", obj);
					// 形参部分设置赋值
					int index =0;
					IdentifierStatement paramIdt = null;
					for(Statement param_state: func_.params)
					{
						paramIdt = (IdentifierStatement)param_state;
						if(index < args.size())
							temp_func_namespace.AddSymbol(paramIdt.name, args.get(index++));
						else
							temp_func_namespace.AddSymbol(paramIdt.name, new LuneObject());	// 传参数量可以和声明的参数不一致的。
					}
					// 将命名空间放入栈顶
					rt.PushNamespace(temp_func_namespace);
					func_.OnFunctionCall(rt, null);
					// 函数执行完成后移除命名空间
					rt.PopNamespace();
				}
				return obj;
			}
			// 函数调用
			else if(func.objType == LuneObjectType.FUNCTION)
			{
				LuneFunction func_obj = (LuneFunction)func;
				FunctionExpression func_ = (FunctionExpression) func_obj.GetValue();
				LuneNamespace temp_func_namespace = new LuneNamespace(LuneNamespaceType.FUNCTION, rt.CurrentNamespace());
				if(func_obj.namespace != null)
				{
					temp_func_namespace.UpdateNamespace(func_obj.namespace);
				}
				temp_func_namespace.AddSymbol("arguments", args);
				int index =0;
				IdentifierStatement paramIdt = null;
				for(Statement param_state: func_.params)
				{
					paramIdt = (IdentifierStatement)param_state;
					if(index < args.size())
						temp_func_namespace.AddSymbol(paramIdt.name, args.get(index++));
					else
						temp_func_namespace.AddSymbol(paramIdt.name, new LuneObject());	// 传参数量可以和声明的参数不一致的。
				}
				if(object != null)
				{
					if(object.objType == LuneObjectType.INSTANCE)
						temp_func_namespace.AddSymbol("this", object);
					else if(object.objType == LuneObjectType.CLASS)
					{
						LuneObject ist = rt.CurrentNamespace().GetSymbol("this");
						if(ist != null)
						{
							if(ist.objType != LuneObjectType.INSTANCE)
								throw new RuntimeException();
							// 这里还要检查类继承对不对
							temp_func_namespace.AddSymbol("this", ist);
						}
					}
				}
				// 将命名空间放入栈顶
				rt.PushNamespace(temp_func_namespace);
				LuneObject res = func_.OnFunctionCall(rt, null);
				// 函数执行完成后移除命名空间
				rt.PopNamespace();
				return res;
			}else
			{
				throw new RuntimeException();
			}
		}
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
}
