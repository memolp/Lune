package org.jeff.lune.parsers.objs;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;

/**
 * 字符串常量表达式
 * TODO 需要处理 " ", "\"" , '"', "''" 单双引号组合转义的显示以及UTF8格式
 * @author 覃贵锋
 *
 */
public class StringStatement extends Statement
{
	public String value;
	/**
	 * 字符串常量表达式
	 * @param val
	 * @param line
	 * @param col
	 */
	public StringStatement(String val, int line, int col)
	{
		super(StatementType.STRING, line, col);
		this.value = val;
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 创建字符串对象
		return new LuneObject(value);
	}
	
	@Override
	public String toString() 
	{
		return this.value;
	}

}
