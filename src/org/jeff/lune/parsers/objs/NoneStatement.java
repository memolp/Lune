package org.jeff.lune.parsers.objs;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;

/**
 * None空值
 * @author 覃贵锋
 *
 */
public class NoneStatement extends Statement
{
	/** 空值是固定，因此只需要记录行列 */
	public NoneStatement(int line, int col)
	{
		super(StatementType.NONE,  line, col);
	}

	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object)
	{
		// 返回常量
		return LuneObject.noneLuneObject;
	}

}
