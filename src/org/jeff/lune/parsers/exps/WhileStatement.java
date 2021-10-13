package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

/**
 * while循环
 * @author 覃贵锋
 *
 */
public class WhileStatement extends Statement
{
	/** 循环的条件 */
	public Statement condition;
	/** 循环的内部体 */
	public BlockStatement body;
	/**
	 * while语句
	 * @param line
	 * @param col
	 */
	public WhileStatement(int line, int col)
	{
		super(StatementType.WHILE, line, col);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		LuneObject res = null;
		// while 语句的执行
		do
		{
			// 先执行条件，然后看条件是否为true
			res = this.condition.OnExecute(rt, null);
			if(res != null && res.toBool())
			{
				this.body.OnExecute(rt, null);
				// 遇到break和return就跳出循环
				if(rt.IsBreakFlag || rt.IsReturnFlag)
				{
					break;
				}
				// 每次执行完body都需要重置continue
				rt.IsContinueFlag = false;
			}
			else
			{
				break; //条件不满足则跳出循环
			}
		}while(true);
		// 循环结束后要重置break和continue标记
		rt.IsBreakFlag = false;
		rt.IsContinueFlag = false;
		return res;
	}
}
