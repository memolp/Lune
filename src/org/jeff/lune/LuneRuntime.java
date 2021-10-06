package org.jeff.lune;

import java.util.HashMap;
import java.util.Map;

import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.parsers.BlockStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.parsers.exps.AssignmentExpression;
import org.jeff.lune.parsers.exps.BinaryExpression;
import org.jeff.lune.parsers.exps.CallExpression;
import org.jeff.lune.parsers.exps.FunctionExpression;
import org.jeff.lune.parsers.exps.ListExpression;
import org.jeff.lune.parsers.exps.MapExpression;
import org.jeff.lune.parsers.objs.IdentifierStatement;
import org.jeff.lune.parsers.objs.NumberStatement;
import org.jeff.lune.parsers.objs.StringStatement;

public class LuneRuntime 
{
	/** 命名空间 */
	Map<String, LuneObject> mNamespaces = new HashMap<String, LuneObject>();
	public void AddParam(String name, String value)
	{
			mNamespaces.put(name, new LuneObject(value));
	}
	public void AddParam(String name, Object value)
	{
			mNamespaces.put(name, new LuneObject(value));
	}
	/**
	 * 执行语句
	 * @param state
	 * @return
	 */
	public void Execute(Statement state)
	{
		LuneObject res = null;
		switch(state.statementType)
		{
			case BLOCK:
				this.ExecuteBlock((BlockStatement)state);
				break;
			case ASSIGNMENT:
				res = this.ExecuteAssignment((AssignmentExpression)state);
				 break;
			case EXPRESSION_BINARY:
				res = this.ExecuteExpression((BinaryExpression) state);
				 break;
			case FUNCCALL:
				res = this.ExecuteFunctionCall((CallExpression) state);
			default:
				break;
		}
		if(res != null)
			System.out.println(res.toString());
	}
	
	protected LuneObject ExecuteFunctionCall(CallExpression state) 
	{
		Statement name = state.variable;
		if(name.statementType == StatementType.IDENTIFIER)
		{
			
		}
		return null;
	}
	protected LuneObject ExecuteAssignment(AssignmentExpression state) 
	{
		Statement left = state.variable;
		Statement right = state.value;
		// 找到这个变量,如果没有需要创建
		LuneObject object = null;
		// 处理左侧的变量，可能是简单变量，可能是某个属性变量，或者数组，列表的索引。
		if(left.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement identify = (IdentifierStatement)left;
			object = this.mNamespaces.get(identify.name);	// TODO 局部和全局变量
			if(object == null)
			{
				object = new LuneObject();
				this.mNamespaces.put(identify.name, object);
			}
		}
		
		// 处理右侧的值
		if(right.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement identify = (IdentifierStatement)right;
			LuneObject  value = this.mNamespaces.get(identify.name);  //TODO 局部和全局变量
			if(value == null) // 找不到变量则报错.
				throw new RuntimeException();
			object.SetValue(value);
		}else if(right.statementType == StatementType.NUMBER)
		{
			NumberStatement num = (NumberStatement)right;
			object.SetValue(num.value);
		}else if(right.statementType == StatementType.STRING)
		{
			StringStatement str = (StringStatement)right;
			object.SetValue(str.value);
		}else if(right.statementType == StatementType.LIST_OBJECT)
		{
			//TODO 将列表表达式进行处理
			ListExpression list = (ListExpression)right;
			object.SetValue(list);
		}else if(right.statementType == StatementType.MAP_OBJECT)
		{
			MapExpression map = (MapExpression)right;
			object.SetValue(map);
		}else if(right.statementType == StatementType.FUNCTION)
		{
			FunctionExpression func = (FunctionExpression)right;
			object.SetValue(func);
		}
		return object;
	}
	/**
	 * 执行语句块
	 * @param bstate
	 */
	public void ExecuteBlock(BlockStatement bstate)
	{
		for(Statement state: bstate.body)
		{
			this.Execute(state);
		}
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
		}else if(left.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement idt = (IdentifierStatement)left;
			obj1 = this.mNamespaces.get(idt.name);
		}else if(left.statementType == StatementType.NUMBER)
		{
			obj1 = new LuneObject(((NumberStatement)left).value);
		}
		
		// 处理右侧的变量
		if(right.statementType == StatementType.EXPRESSION_BINARY)
		{
			obj2 = this.ExecuteExpression((BinaryExpression)right);
		}else if(right.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement idt = (IdentifierStatement)right;
			obj2 = this.mNamespaces.get(idt.name);
		}
		else if(right.statementType == StatementType.NUMBER)
		{
			obj2 = new LuneObject(((NumberStatement)right).value);
		}
		
		if(obj1 ==null || obj2 == null)
			throw new RuntimeException();
		
		if(obj1.objType == LuneObjectType.NUMBER)
		{
			 if(obj2.objType == LuneObjectType.NUMBER)
			 {
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
			 }else
			 {
				 
			 }
		}
		return null;
	}
	
	
}
