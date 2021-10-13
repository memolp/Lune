package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

/**
 * break跳出循环的语句
 * @author 覃贵锋
 *
 */
public class BreakStatement extends Statement 
{
	/**
	 * break语句
	 * @param line
	 * @param col
	 */
	public BreakStatement(int line, int col)
	{
		super(StatementType.BREAK, line, col);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 没有执行行为，外部遇到就走跳出
		return null;
	}

}
