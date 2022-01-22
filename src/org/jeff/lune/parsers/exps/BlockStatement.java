package org.jeff.lune.parsers.exps;

import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

/**
 * 语句块，存放当面快的全部语句列表
 * @author 覃贵锋
 *
 */
public class BlockStatement extends Statement
{
	/** 语句列表 */
	public List<Statement> body = new LinkedList<Statement>();
	/**
	 * 语句块
	 */
	public BlockStatement(int line, int col)
	{
		super(StatementType.BLOCK, line, col);
	}
	/**
	 * 添加语句
	 * @param state
	 */
	public void AddStatement(Statement state)
	{
		body.add(state);
	}
	/**
	 * 语句块执行，包含文件、函数、各种控制语句
	 */
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 这是整个代码执行的基础，基本上所有的代码都是由语句块开始执行
		// 返回的结果
		LuneObject res = LuneObject.noneLuneObject;
		// 遍历全部的语句进行依次执行
		for(Statement state: this.body)
		{
			// 遇到break、continue、return就会中断整个语句块的执行，跳出当前语句块。
			// 是否继续执行则有外部语句块控制
			if(rt.IsBreakFlag || rt.IsContinueFlag || rt.IsReturnFlag)
				break;
			// 赋值语句
			if(state.statementType == StatementType.ASSIGNMENT)
				res = state.OnExecute(rt, object);
			// 属性调用 -- 这种一般是继续执行，例如 a.b() a.b =c等之类的
			else if(state.statementType == StatementType.MEMBER)
				res = state.OnExecute(rt, object);
			// if语句，内部执行的时候就会自动进行分支判断
			else if(state.statementType == StatementType.IF)
				state.OnExecute(rt, object);
			// while循环，内部进行循环控制
			else if(state.statementType == StatementType.WHILE)
				state.OnExecute(rt, object);
			// for迭代器
			else if(state.statementType == StatementType.FOR)
				state.OnExecute(rt, object);
			// index下标访问-主要是列表和字典 - 其行为类似MEMBER
			else if(state.statementType == StatementType.INDEX)
				res = state.OnExecute(rt, object);
			// 函数调用
			else if(state.statementType == StatementType.FUNCCALL)
				res = state.OnExecute(rt, object);
			// 二元表达式
			else if(state.statementType == StatementType.EXPRESSION_BINARY)
				res = state.OnExecute(rt, object);
			// 一元表达式
			else if(state.statementType == StatementType.EXPRESSION_UNARY)
				res = state.OnExecute(rt, object);
			// 函数的return
			else if(state.statementType == StatementType.RETURN)
			{
				// 检查return是不是在函数内部。
				if(!state.isInFunctionBlock())
				{
					rt.RuntimeError(this, "%s 只能放在函数内部", state.toString());
				}
				res = state.OnExecute(rt, object);
				// 设置RT进入跳出运行 必须跳出直到遇到函数语句块则结束
				rt.IsReturnFlag = true;
				break;
			}
			// 循环的break
			else if(state.statementType == StatementType.BREAK)
			{
				// break 要在循环里面
				if(!state.isInLoopBlock())
				{
					rt.RuntimeError(this, "%s 只能放在循环体内部", state.toString());
				}
				rt.IsBreakFlag = true;
				break;
			}
			// 循环的continue
			else if(state.statementType == StatementType.CONTINUE)
			{
				// continue也必须在循环里面
				if(!state.isInLoopBlock())
				{
					rt.RuntimeError(this, "%s 只能放在循环体内部", state.toString());
				}
				rt.IsContinueFlag = true;
				break;
			}
		}
		// 返回结果
		return res;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		for(Statement state : body)
		{
			sb.append(state.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
