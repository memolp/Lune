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
		// if 语句不可能是 xx.if 这样的形式，因此不存在调用者
		if(this.statementType  != StatementType.ELSE)
		{
			LuneObject res = this.condition.OnExecute(rt, null);
			// 条件结果为true则运行if内部语句块  有可能res是空值。
			try
			{
				if(res.toBool()) 
				{
					// 语句块内部按照语句类型再进行执行
					this.body.OnExecute(rt, null);
				}else
				{
					if(this.Switch != null)  // 如果有分支则走分支判断
					{
						if(this.Switch.statementType == StatementType.ELIF)
						{
							 this.Switch.OnExecute(rt, null);
						}else if(this.Switch.statementType == StatementType.ELSE)
						{
							this.Switch.OnExecute(rt, null);
						}else
						{
							rt.RuntimeError(this, "语句: %s 类型:%s 并不是分支语句", this.Switch, this.Switch.statementType);
						}
					}
				}
			}catch (Exception e) {
				rt.RuntimeError(this, "%s", e.getMessage());
			}
		}else // 否则ELSE条件不需要判断条件，执行运行
		{
			this.body.OnExecute(rt, null);
		}
		return LuneObject.noneLuneObject;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("if");
		sb.append(this.condition.toString());
		sb.append("");
		return sb.toString();
	}
}
