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
	/**
	 * 赋值语句
	 * 变量赋值
	 * 属性赋值
	 * 列表赋值
	 * 字典赋值
	 */
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// object 为null ==> a= xx
		// object 不为null ==> a.x = xx
		if(variable.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement idt = (IdentifierStatement)variable;
			// 赋值语句出来的是需要更新左边的变量的。
			LuneObject val = this.value.OnExecute(rt, null);
			if(object == null)
			{
				//idt.cache_value = val;  // 缓存值
				rt.CurrentNamespace().AddSymbol(idt.name, val);
			}else
			{
				object.SetAttribute(idt.name, val);
			}
		}
		// 对列表和字典的赋值a[1] =1 a['k'] = 1
		else if(variable.statementType == StatementType.INDEX)
		{
			IndexExpression listexp = (IndexExpression)variable;  // IndexExpression本身的OnExecute是返回索引处的值，但是我们这里是要赋值，因此拆出来。
			LuneObject _obj =  listexp.object.OnExecute(rt, object);
			LuneObject val = this.value.OnExecute(rt, null);
			if(_obj.objType == LuneObjectType.LIST)
			{
				LuneListObject list = (LuneListObject)_obj;
				try
				{
					// 获取下标
					int index = (int) listexp.index.OnExecute(rt, null).longValue();
					list.Set(index, val);
				} catch (Exception e)
				{
					rt.RuntimeError(this, "%s", e.getMessage());
				}
			}
			else if(_obj.objType == LuneObjectType.MAP)
			{
				LuneMapObject map = (LuneMapObject)_obj;
				// 获取key
				LuneObject index = listexp.index.OnExecute(rt, null);
				map.Set(index, val);
			}
		}else
		{
			rt.RuntimeError(this, "%s 语句语法错误", this.toString());
		}
		return LuneObject.noneLuneObject;
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
