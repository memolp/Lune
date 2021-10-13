package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

/**
 * continue循环继续执行表达式
 * @author 覃贵锋
 *
 */
public class ContinueStatement extends Statement 
{
	/**
	 * continue
	 * @param line
	 * @param col
	 */
	public ContinueStatement(int line, int col)
	{
		super(StatementType.CONTINUE, line, col);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// continue 也没有执行行为
		return null;
	}

}
