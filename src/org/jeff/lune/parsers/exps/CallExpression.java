package org.jeff.lune.parsers.exps;

import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.LuneNamespace;
import org.jeff.lune.LuneNamespaceType;
import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneClassObject;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneFunction;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

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
	/**
	 * 函数调用，根据不同的类型进行处理
	 * 1. Java导出的EXECUTEABLE类型
	 * 2. Lune中call一个定义的类（相当于创建类的实例） 
	 *    例如：
	 *       A = class("a")
	 *       a = A()  # 这里就是对类进行call，生成实例。
	 * 3. 函数调用
	 *     普通的函数调用 test()
	 *     类的静态方法 A.test()
	 *     成员方法调用 a.test()
	 */
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		LuneObject result = LuneObject.noneLuneObject;
		// 函数调用分为 三类 Java导出的可执行方法， 类执行，其他函数执行
		// 获取函数名对象
		LuneObject func = variable.OnExecute(rt, object);
		if(func.objType == LuneObjectType.None) 
		{
			rt.RuntimeError(this, "%s 符号未找到定义.",  variable);
		}
		// Java 执行方法 - 这个是Java那边导出的。
		if(func.objType == LuneObjectType.EXECUTEABLE)
		{
			// 先处理传参部分，根据函数的形参生成实参数据。
			LuneObject[] args = new LuneObject[this.params.size()];
			LuneObject temp_args = null;
			int index = 0;
			// 处理传参部分 
			for(Statement param_state: this.params)
			{
				temp_args = param_state.OnExecute(rt, null);
				// 由于允许传null空值，因此这里去掉判断
				/*if(temp_args.objType == LuneObjectType.None)
				{
					rt.RuntimeError(this, "%s 符号参数未找到对应的定义",  param_state);
				}*/
				args[index ++] = temp_args;
			}
			LuneExecuteable impfunc = (LuneExecuteable)func;
			impfunc.callObject = object;  // 如果是math.round 那么round是EXECUTEABLE，调用者是math。需要提供
			try
			{
				result =  impfunc.Execute(rt, args);   // 执行结果并返回。
			} catch (Exception e)
			{
				rt.RuntimeError(this, "%s", e.getMessage());
			}
		}
		else
		{
			// 对类执行调用，相当于创建对象
			if(func.objType == LuneObjectType.CLASS)
			{
				LuneClassObject clsDef = (LuneClassObject)func;
				try
				{
					result = clsDef.Exceute(rt, this.params);
				} catch (Exception e)
				{
					rt.RuntimeError(this, "%s", e.getMessage());
				}
			}
			// 函数调用
			else if(func.objType == LuneObjectType.FUNCTION)
			{
				LuneFunction func_obj = (LuneFunction)func;
				// 函数的命名空间放在外部创建，主要是方便提供额外的数据进去。
				LuneNamespace temp_func_namespace = new LuneNamespace(LuneNamespaceType.FUNCTION, rt.CurrentNamespace());
				// 将命名空间放入栈顶
				rt.PushNamespace(temp_func_namespace);
				boolean need_self_symbol = false;
				// 如何函数调用是someobj.call() 这样的，就需要对someobj进行判断
				if(object != null)
				{
					// 如果someobj是类的实例对象，就需要提供默认的self进去
					if(object.objType == LuneObjectType.INSTANCE)
					{
						need_self_symbol = true;
						temp_func_namespace.AddSymbol("self", object);
					}
					// 如果someobj是类，那边只有两种可能 调用静态方法，或者调用父类的方法。
					else if(object.objType == LuneObjectType.CLASS)
					{
						// TODO 这里后面加安全检查吧，比如这个object Class 是不是 self的父类。
						need_self_symbol = false;
						/*LuneObject ist = rt.CurrentNamespace().GetSymbol("this");
						if(ist != null)
						{
							if(ist.objType != LuneObjectType.INSTANCE)
							{
								rt.RuntimeError(this, "this 必须是实例对象，当前获取的是:%s 类型",  ist.objType);
							}
							// 这里还要检查类继承对不对
							temp_func_namespace.AddSymbol("this", ist);
						}*/
					}
				}
				try
				{
					result = func_obj.Exceute(rt, this.params, need_self_symbol);
				} catch (Exception e)
				{
					rt.RuntimeError(this, "%s", e.getMessage());
				}
				// 函数执行完成后移除命名空间
				rt.PopNamespace();
			}else
			{
				rt.RuntimeError(this, "%s 不是可以调用的对象", func);
			}
		}
		return result;
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
