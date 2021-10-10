package org.jeff.lune.parsers;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

public class IfElseStatement extends Statement
{
	public Statement condition = null;
	public IfElseStatement Switch = null;
	public BlockStatement body;
	/**
	 * 需要指定语句类型
	 * @param stype
	 */
	public IfElseStatement(StatementType stype)
	{
		this.statementType = stype;
	}
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// if 语句不可能是 xx.if 这样的形式，因此不存在调用者
		if(this.statementType  != StatementType.ELSE)
		{
			LuneObject res = this.condition.OnExecute(rt, null);
			if(res.toBool()) // 条件结果为true则运行if内部语句块
			{
				return this.body.OnExecute(rt, null);
			}else
			{
				if(this.Switch == null) return null;  // 说明没有分支
				if(this.Switch.statementType == StatementType.ELIF)
				{
					return this.Switch.OnExecute(rt, null);
				}else if(this.Switch.statementType == StatementType.ELSE)
				{
					return this.Switch.OnExecute(rt, null);
				}else
				{
					throw new RuntimeException();
				}
			}
		}else
		{
			return this.body.OnExecute(rt, null);
		}
	}
}
