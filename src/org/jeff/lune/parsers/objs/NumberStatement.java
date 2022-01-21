package org.jeff.lune.parsers.objs;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;

/**
 * 数字 目前仅支持浮点数double类型，如果需要整形的再搞一下。
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
	public NumberStatement(String val, boolean isFloat, int line, int col)
	{
		super(StatementType.NUMBER, line, col);
		if(isFloat)
			this.value = LuneObject.CreateDoubleObject(Double.parseDouble(val));
		else
			this.value = LuneObject.CreateLongObject(Long.parseLong(val));
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
