package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.parsers.objs.IdentifierStatement;

/**
 * 赋值表达式
 * @author 覃贵锋
 *
 */
public class AssignmentExpression extends ExpressionStatement 
{
	/** 左边变量 */
	public Statement variable;
	/** 右边值 */
	public Statement value;
	/**
	 * 赋值表达式
	 */
	public AssignmentExpression()
	{
		this.statementType = StatementType.ASSIGNMENT;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append(variable.toString());
		sb.append(" = ");
		sb.append(value.toString());
		return sb.toString();
	}

	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// object 为null ==> a= xx
		// object 不为null ==> a.x = xx
		if(variable.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement idt = (IdentifierStatement)variable;
			LuneObject val = this.value.OnExecute(rt, null);
			if(val == null) throw new RuntimeException();
			if(object == null)
			{
				rt.CurrentNamespace().AddSymbol(idt.name, val);
			}else
			{
				object.SetAttribute(idt.name, val);
			}
			return val;
		}else if(variable.statementType == StatementType.INDEX)
		{
			IndexExpression listexp = (IndexExpression)variable;
			LuneObject _obj = rt.GetLuneObject(listexp.object, object);
			LuneObject val = this.value.OnExecute(rt, null);
			if(_obj.objType == LuneObjectType.LIST)
			{
				LuneListObject list = (LuneListObject)_obj;
				if(listexp.index.statementType != StatementType.NUMBER) throw new RuntimeException();
				int index = (int) listexp.index.OnExecute(rt, null).toLong();
				list.Set(index, val);
			}
			return _obj;
		}else
		{
			throw new RuntimeException();
		}
	}
}
