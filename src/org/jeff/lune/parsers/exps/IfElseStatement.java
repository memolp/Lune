package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

/**
 * 条件分支表达式
 * if(xx){} elif(xx) {} else{}
 * @author 覃贵锋
 *
 */
public class IfElseStatement extends Statement
{
	/** 条件表达式 */
	public Statement condition = null;
	/** 其他分支记录 */
	public IfElseStatement Switch = null;
	/** 当前分支的语句块 */
	public BlockStatement body;
	/**
	 * 需要指定语句类型
	 * @param stype
	 */
	public IfElseStatement(StatementType stype, int line, int col)
	{
		super(stype, line, col);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		LuneObject res = null;
		// if 语句不可能是 xx.if 这样的形式，因此不存在调用者
		if(this.statementType  != StatementType.ELSE)
		{
			res = this.condition.OnExecute(rt, null);
			if(res.toBool()) // 条件结果为true则运行if内部语句块
			{
				// 语句块内部按照语句类型再进行执行
				//rt.PushBlockType(BlockStatementType.IFELSE_BLOCK);
				res = this.body.OnExecute(rt, null);
				//rt.PopBlockType();
				return res;
			}else
			{
				if(this.Switch == null) return null;  // 说明没有分支
				if(this.Switch.statementType == StatementType.ELIF)
				{
					//rt.PushBlockType(BlockStatementType.IFELSE_BLOCK);
					res = this.Switch.OnExecute(rt, null);
					//rt.PopBlockType();
					return res;
				}else if(this.Switch.statementType == StatementType.ELSE)
				{
					//rt.PushBlockType(BlockStatementType.IFELSE_BLOCK);
					res = this.Switch.OnExecute(rt, null);
					//rt.PopBlockType();
					return res;
				}else
				{
					throw new RuntimeException();
				}
			}
		}else // 否则ELSE条件不需要判断条件，执行运行
		{
			//rt.PushBlockType(BlockStatementType.IFELSE_BLOCK);
			res = this.body.OnExecute(rt, null);
			//rt.PopBlockType();
			return res;
		}
	}
}
