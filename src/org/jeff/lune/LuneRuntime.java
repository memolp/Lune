package org.jeff.lune;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jeff.lune.object.LuneFunction;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.object.LunePrintFunc;
import org.jeff.lune.object.LuneStringModule;
import org.jeff.lune.parsers.BlockStatement;
import org.jeff.lune.parsers.ReturnStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.parsers.exps.AssignmentExpression;
import org.jeff.lune.parsers.exps.BinaryExpression;
import org.jeff.lune.parsers.exps.CallExpression;
import org.jeff.lune.parsers.exps.FunctionExpression;
import org.jeff.lune.parsers.exps.ListExpression;
import org.jeff.lune.parsers.exps.MapExpression;
import org.jeff.lune.parsers.exps.MemberExpression;
import org.jeff.lune.parsers.objs.IdentifierStatement;
import org.jeff.lune.parsers.objs.NumberStatement;
import org.jeff.lune.parsers.objs.StringStatement;

public class LuneRuntime 
{
	LuneNamespace mGlobalNamespaces;
	LuneNamespace mCurrentNamespaces;
	public LuneRuntime()
	{
		mGlobalNamespaces = new LuneNamespace();
		mGlobalNamespaces.AddSymbol("print", new LunePrintFunc());
		mGlobalNamespaces.AddSymbol("string", new LuneStringModule());
		mCurrentNamespaces = mGlobalNamespaces;
	}

	/**
	 * 执行语句
	 * @param state
	 * @return
	 */
	public LuneObject Execute(Statement state, LuneObject object)
	{
		LuneObject res = null;
		switch(state.statementType)
		{
			case BLOCK:
				res = this.ExecuteBlock((BlockStatement)state);
				break;
			case ASSIGNMENT:
				res = this.ExecuteAssignment((AssignmentExpression)state, object);
				 break;
			case MEMBER:
				this.ExecuteMember((MemberExpression)state, object);
				break;
			case EXPRESSION_BINARY:
				res = this.ExecuteExpression((BinaryExpression) state);
				 break;
			case FUNCCALL:
				res = this.ExecuteFunctionCall((CallExpression) state, object);
				break;
			case RETURN:
				if(mCurrentNamespaces == mGlobalNamespaces)
					throw new RuntimeException();
				res = this.Execute(((ReturnStatement)state).expression, object);
				break;
			default:
				break;
		}
//		if(res != null)
//			System.out.println(res.toString());
		return res;
	}
	
	protected void ExecuteMember(MemberExpression state, LuneObject object)
	{
		if(state.parent.statementType != StatementType.IDENTIFIER)
			throw new RuntimeException();
		IdentifierStatement identify = (IdentifierStatement)state.parent;
		LuneObject obj  = null;
		// 
		if(object == null)
			obj = mCurrentNamespaces.GetSymbol(identify.name);
		else
			obj = object.GetAttribute(identify.name);
		if(obj == null || (obj.objType != LuneObjectType.LIST && obj.objType != LuneObjectType.MAP && obj.objType != LuneObjectType.OBJECT))
			throw new RuntimeException();
		this.Execute(state.child, obj);
	}

	protected LuneObject ExecuteFunctionCall(CallExpression state, LuneObject object) 
	{
		LuneObject func = this.GetLuneObject(state.variable, object);
		if(func.objType == LuneObjectType.EXECUTEABLE)
			return this.ExecuteImpFunctionCall((LuneExecuteable) func ,state);
		if(func.objType != LuneObjectType.FUNCTION)
			throw new RuntimeException();
		FunctionExpression func_ = (FunctionExpression) func.GetValue();
		LuneNamespace temp_func_namespace = new LuneNamespace(mCurrentNamespaces);
		List<LuneObject> args = new LinkedList<LuneObject>();
		LuneObject temp_args = null;
		// 处理传参部分
		for(Statement param_state: state.params.params)
		{
			if(param_state.statementType == StatementType.NUMBER)
			{
				temp_args = new LuneObject(((NumberStatement)param_state).value);
			}else if(param_state.statementType == StatementType.STRING)
			{
				temp_args = new LuneObject(((StringStatement)param_state).value);
			}else if(param_state.statementType == StatementType.EXPRESSION_BINARY)
			{
				temp_args = this.ExecuteExpression((BinaryExpression) param_state);
			}else if(param_state.statementType == StatementType.FUNCCALL) // 传参里面函数调用
			{
				temp_args = this.ExecuteFunctionCall((CallExpression) param_state, null);
			}else if(param_state.statementType == StatementType.FUNCTION)
			{
				temp_args = new LuneFunction((FunctionExpression) param_state);
			}
			else // 这里应该只有变量，属性访问，索引
			{
				temp_args = this.GetLuneObject(param_state, null);
			}
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
		LuneNamespace before_nap = mCurrentNamespaces;
		mCurrentNamespaces = temp_func_namespace;
	
		LuneObject res = this.ExecuteBlock(func_.body);
		mCurrentNamespaces = before_nap;
		return res;
	}
	
	protected LuneObject ExecuteImpFunctionCall(LuneExecuteable impfunc, CallExpression state) 
	{
		LuneNamespace temp_func_namespace = new LuneNamespace(mCurrentNamespaces);
		LuneObject[] args = new LuneObject[state.params.params.size()];
		LuneObject temp_args = null;
		int index = 0;
		// 处理传参部分
		for(Statement param_state: state.params.params)
		{
			if(param_state.statementType == StatementType.NUMBER)
			{
				temp_args = new LuneObject(((NumberStatement)param_state).value);
			}else if(param_state.statementType == StatementType.STRING)
			{
				temp_args = new LuneObject(((StringStatement)param_state).value);
			}else if(param_state.statementType == StatementType.EXPRESSION_BINARY)
			{
				temp_args = this.ExecuteExpression((BinaryExpression) param_state);
			}else if(param_state.statementType == StatementType.FUNCCALL) // 传参里面函数调用
			{
				temp_args = this.ExecuteFunctionCall((CallExpression) param_state, null);
			}else if(param_state.statementType == StatementType.FUNCTION)
			{
				temp_args = new LuneFunction((FunctionExpression) param_state);
			}
			else // 这里应该只有变量，属性访问，索引
			{
				temp_args = this.GetLuneObject(param_state, null);
			}
			if(temp_args == null)
				throw new RuntimeException();
			args[index++] = temp_args;
		}
		return impfunc.Execute(args);
	}
	
	protected LuneObject ExecuteAssignment(AssignmentExpression state, LuneObject object) 
	{
		Statement left = state.variable;
		Statement right = state.value;
		// 处理左侧的变量 到最后都只是标识符
		if(left.statementType != StatementType.IDENTIFIER)
		{
			throw new RuntimeException();
		}
		LuneObject lefVariable = null;
		IdentifierStatement identify = (IdentifierStatement)left;
		if(object == null)
		{
			// 获取变量的时候会递归查找
			lefVariable = mCurrentNamespaces.GetSymbol(identify.name);
			if(lefVariable == null)
			{
				lefVariable = new LuneObject();
				// 添加符号变量的时候只会加在当前符号表上。
				mCurrentNamespaces.AddSymbol(identify.name, lefVariable);
			}
		}else
		{
			lefVariable = object.GetAttribute(identify.name);
			if(lefVariable == null)
			{
				lefVariable = new LuneObject();
				object.SetAttribute(identify.name, lefVariable);
			}
		}
		// 处理右侧的值
		if(right.statementType == StatementType.IDENTIFIER)
		{
			identify = (IdentifierStatement)right;
			LuneObject  value = mCurrentNamespaces.GetSymbol(identify.name);  //TODO 局部和全局变量
			if(value == null) // 找不到变量则报错.
				throw new RuntimeException();
			lefVariable.SetValue(value);
		}else if(right.statementType == StatementType.NUMBER)
		{
			NumberStatement num = (NumberStatement)right;
			lefVariable.SetValue(num.value);
		}else if(right.statementType == StatementType.STRING)
		{
			StringStatement str = (StringStatement)right;
			lefVariable.SetValue(str.value);
		}else if(right.statementType == StatementType.LIST_OBJECT)
		{
			//TODO 将列表表达式进行处理
			ListExpression list = (ListExpression)right;
			lefVariable.SetValue(list);
		}else if(right.statementType == StatementType.MAP_OBJECT)
		{
			MapExpression map = (MapExpression)right;
			lefVariable.SetValue(map);
		}else if(right.statementType == StatementType.FUNCTION)
		{
			LuneFunction func = new LuneFunction((FunctionExpression)right);
			lefVariable.SetValue(func);
		}else if(right.statementType == StatementType.FUNCCALL)
		{
			lefVariable.SetValue(this.ExecuteFunctionCall((CallExpression) right, null));
		}
		return lefVariable;
	}
	/**
	 * 执行语句块
	 * @param bstate
	 */
	public LuneObject ExecuteBlock(BlockStatement bstate)
	{
		LuneObject res = null;
		for(Statement state: bstate.body)
		{
			res = this.Execute(state, null);
		}
		return res;
	}
	
	public LuneObject ExecuteExpression(BinaryExpression bexp)
	{
		Statement left = bexp.left;
		Statement right = bexp.right;
		LuneObject obj1 = null, obj2 = null;
		// 处理左侧的变量
		if(left.statementType == StatementType.EXPRESSION_BINARY)
		{
			obj1 = this.ExecuteExpression((BinaryExpression)left);
		}
		else if(left.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement idt = (IdentifierStatement)left;
			obj1 = mCurrentNamespaces.GetSymbol(idt.name);
		}else if(left.statementType == StatementType.NUMBER)
		{
			obj1 = new LuneObject(((NumberStatement)left).value);
		}else if(left.statementType == StatementType.MEMBER)
		{
			obj1 = this.GetMember((MemberExpression) left, null);
		}
		
		// 处理右侧的变量
		if(right.statementType == StatementType.EXPRESSION_BINARY)
		{
			obj2 = this.ExecuteExpression((BinaryExpression)right);
		}else if(right.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement idt = (IdentifierStatement)right;
			obj2 = mCurrentNamespaces.GetSymbol(idt.name);
		}
		else if(right.statementType == StatementType.NUMBER)
		{
			obj2 = new LuneObject(((NumberStatement)right).value);
		}else if(right.statementType == StatementType.MEMBER)
		{
			obj2 = this.GetMember((MemberExpression) right, null);
		}else if(right.statementType == StatementType.FUNCCALL)
		{
			obj2 = this.ExecuteFunctionCall((CallExpression) right, null);
		}
		
		if(obj1 ==null || obj2 == null)
			throw new RuntimeException();
		
		if(obj1.objType != LuneObjectType.NUMBER || obj2.objType != LuneObjectType.NUMBER)
			throw new RuntimeException();
		
			LuneObject res = new LuneObject(0.0f);
		switch(bexp.opType)
		{
		 	case  OP_PLUS:
		 		res.SetValue(obj1.toNumber() + obj2.toNumber());
		 		return res;
		 	case OP_MINUS:
		 		res.SetValue(obj1.toNumber() -  obj2.toNumber());
		 		return res;
		 	case OP_MULTI:
		 		res.SetValue(obj1.toNumber() * obj2.toNumber());
		 		return res;
		 	case OP_DIV:
		 		res.SetValue(obj1.toNumber() / obj2.toNumber());
		 		return res;
		 	case OP_MOD:
		 		res.SetValue(obj1.toNumber() % obj2.toNumber());
		 		return res;
		 	case OP_BIT_AND:
		 		res.SetValue(obj1.toLong() & obj2.toLong());
		 		return res;
		 	default:
		 		throw new RuntimeException();
		}
	}
	
	public LuneObject GetLuneObject(Statement state, LuneObject object)
	{
		if(state.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement idt = (IdentifierStatement)state;
			if(object == null)
			{
				return mCurrentNamespaces.GetSymbol(idt.name);
			}else
			{
				return object.GetAttribute(idt.name);
			}
		}else if(state.statementType == StatementType.MEMBER)
		{
			MemberExpression mem = (MemberExpression)state;
			return this.GetMember(mem, object);
		}else if(state.statementType == StatementType.INDEX)
		{
			return null;
		}else if(state.statementType == StatementType.NUMBER)
		{
			return new LuneObject(((NumberStatement)state).value);
		}else if(state.statementType == StatementType.STRING)
		{
			return new LuneObject(((StringStatement)state).value);
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
	public LuneObject GetMember(MemberExpression exp, LuneObject object)
	{
		if(exp.parent.statementType != StatementType.IDENTIFIER)
			throw new RuntimeException();
		
		LuneObject parent = null;
		IdentifierStatement idt = (IdentifierStatement)exp.parent;
		if(object == null)
		{
			parent = mCurrentNamespaces.GetSymbol(idt.name);
		}else
		{
			parent = object.GetAttribute(idt.name);
		}
		if(parent == null)
			throw new RuntimeException();
		if(exp.child.statementType == StatementType.IDENTIFIER)
			return parent.GetAttribute(((IdentifierStatement)exp.child).name);
		else if(exp.child.statementType == StatementType.MEMBER)
		{
			return this.GetMember((MemberExpression) exp.child, parent);
		}else if(exp.child.statementType == StatementType.FUNCCALL)
		{
			return this.ExecuteFunctionCall((CallExpression) exp.child, parent);
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
}
