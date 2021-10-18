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
	public BooleanStatement(boolean v)
	{
		super(StatementType.BOOLEAN, -1, -1);
		value = new LuneObject(v);
	}
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object)
	{
		return value;
	}

}
