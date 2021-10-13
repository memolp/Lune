package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

/**
 * 语句的基本类，其他全部的语句都继承自此类
 * @author 覃贵锋
 *
 */
public abstract class Statement
{
	/** 语句的类型 */
	public StatementType statementType;
	/** 语句当前行 */
	public int startLine;
	/** 语句当前列 */
	public int startColoumn;
	/** 标记为整体 - 主要是针对括号的 */
	public boolean isWhole = false;
	/**
	 * 语句创建时需要传入指定的行和列，用于错误显示。
	 * @param type  语句的类型
	 * @param line
	 * @param col
	 */
	public Statement(StatementType type, int line, int col)
	{
		this.statementType = type;
		this.startLine = line;
		this.startColoumn = col;
	}
	/**
	 * 调用语句执行，不同的语句的执行逻辑不同，都将有其子类自己实现
	 * @param rt	运行时
	 * @param object 当前语句所依附的对象-即由谁调用(这种才有效a.x =1, a.x())
	 * @return
	 */
	public abstract  LuneObject OnExecute(LuneRuntime rt, LuneObject object);
}
