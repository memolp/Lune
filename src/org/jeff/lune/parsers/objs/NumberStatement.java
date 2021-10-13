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
	public double value = 0.0f;
	/**
	 * 数字常量-统一为double
	 * @param val
	 * @param line
	 * @param col
	 */
	public NumberStatement(String val, int line, int col)
	{
		super(StatementType.NUMBER, line, col);
		this.value = Double.parseDouble(val);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 数字常量不可能是object引出的
		return new LuneObject(value);
	}
	
	@Override
	public String toString() 
	{
		return String.valueOf(this.value);
	}

}
