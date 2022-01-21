package org.jeff.lune.parsers.objs;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;

/**
 * 布尔值
 * @author 覃贵锋
 *
 */
public class BooleanStatement extends Statement
{
	/** true or false */
	public LuneObject value = null;
	/**
	 * 布尔值
	 * @param str
	 * @param line
	 * @param col
	 */
	public BooleanStatement(String str, int line, int col)
	{
		super(StatementType.BOOLEAN, line, col);
		// 内部的值采用常量赋值。
		value = LuneObject.CreateBooleanObject(Boolean.parseBoolean(str));
	}
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object)
	{
		return value;
	}

}
