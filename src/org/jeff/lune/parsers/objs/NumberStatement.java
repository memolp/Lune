package org.jeff.lune.parsers.objs;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;

/**
 * 数字常量
 * @author 覃贵锋
 *
 */
public class NumberStatement extends Statement
{
	/** 数字对象 */
	public LuneObject value;
	/**
	 * 数字常量-统一为double
	 * @param val
	 * @param line
	 * @param col
	 */
	public NumberStatement(double val)
	{
		super(StatementType.NUMBER, -1, -1);
		this.value = new LuneObject(val);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		return this.value;
	}
	
	@Override
	public String toString() 
	{
		return this.value.toString();
	}

}
