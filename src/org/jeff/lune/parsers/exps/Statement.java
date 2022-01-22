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
	public static String LINE_END = System.getProperty("line.separator");
	public static String CurrentFile = "";
	/** 语句的类型 */
	public StatementType statementType;
	/** 语句当前行 */
	public int startLine;
	/** 语句当前列 */
	public int startColoumn;
	/** 标记为整体 - 主要是针对括号的 */
	public boolean isWhole = false;
	/**
	 *  语句树，因此将树结构放回本身，这样整个索引过程无需外部存储
	 *  逻辑父对象，也就是大层面上面的层级关系。（例如a.b =1 b的父对象是a，但是这个表达式的父对象应该是外部的函数体或其他体） 
	 */
	public Statement logicParent = null;
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
	 * 设置当前语句块的逻辑父对象
	 * @param parent
	 */
	public void SetLogicParent(Statement parent)
	{
		this.logicParent = parent;
	}
	/**
	 * 调用语句执行，不同的语句的执行逻辑不同，都将有其子类自己实现
	 * @param rt	运行时
	 * @param object 当前语句所依附的对象-即由谁调用(这种才有效a.x =1, a.x())
	 * @return
	 */
	public abstract  LuneObject OnExecute(LuneRuntime rt, LuneObject object);
	/**
	 * 递归判断是否在函数内部。
	 * @return
	 */
	public boolean isInFunctionBlock()
	{
		Statement state = this;
		while(state != null)
		{
			if(state.statementType == StatementType.FUNCTION)
			{
				return true;
			}
			state = state.logicParent;
		}
		return false;
	}
	/**
	 * 递归判断是否在循环内部
	 * @return
	 */
	public boolean isInLoopBlock()
	{
		Statement state = this;
		while(state != null)
		{
			if(state.statementType == StatementType.FOR || state.statementType == StatementType.WHILE)
			{
				return true;
			}
			state = state.logicParent;
		}
		return false;
	}
	/**
	 * 堆栈打印
	 * @param sb
	 * @param level
	 */
	public void TraceBack(StringBuilder sb, int level)
	{
		int index = 0;
		int last_line = -1;
		Statement state = this;
		while(state != null && index++ < level)
		{
			if(last_line != state.startLine)
			{
				sb.append(String.format("\t位于 %s (%s:%s) %s", "...", Statement.CurrentFile, state.startLine, LINE_END));
				last_line = state.startLine;
			}
			state = state.logicParent;
		}
	}
}
