package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneMapObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
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
	public AssignmentExpression(int line, int col)
	{
		super(StatementType.ASSIGNMENT, line, col);
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
			if(val == null)
			{
				throw new RuntimeException();
			}
			if(object == null)
			{
				// 处理一下重新赋值全局变量
				LuneObject gval = rt.CurrentNamespace().GetSymbol(idt.name);
				if(gval == null)
					rt.CurrentNamespace().AddSymbol(idt.name, val);
				else
					gval.SetValue(val);
			}else
			{
				object.SetAttribute(idt.name, val);
			}
			return val;
		}
		// 对列表和字典的赋值
		else if(variable.statementType == StatementType.INDEX)
		{
			IndexExpression listexp = (IndexExpression)variable;
			LuneObject _obj = rt.GetLuneObject(listexp.object, object);
			LuneObject val = this.value.OnExecute(rt, null);
			if(_obj.objType == LuneObjectType.LIST)
			{
				LuneListObject list = (LuneListObject)_obj;
				// 获取下标
				int index = (int) listexp.index.OnExecute(rt, null).toLong();
				list.Set(index, val);
			}
			else if(_obj.objType == LuneObjectType.MAP)
			{
				LuneMapObject map = (LuneMapObject)_obj;
				// 获取key
				LuneObject index = listexp.index.OnExecute(rt, null);
				map.Set(index, val);
			}
			return _obj;
		}else
		{
			throw new RuntimeException();
		}
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
}
