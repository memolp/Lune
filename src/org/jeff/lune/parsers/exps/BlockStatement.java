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
	/** 语句块的类型定义- 文件，函数，各种条件内， 循环内 */
	public BlockStatementType blockType;
	/**
	 * 语句块
	 */
	public BlockStatement(BlockStatementType bType, int line, int col)
	{
		super(StatementType.BLOCK, line, col);
		this.blockType = bType;
	}
	/**
	 * 添加语句
	 * @param state
	 */
	public void AddStatement(Statement state)
	{
		body.add(state);
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 这是整个代码执行的基础，基本上所有的代码都是由语句块开始执行
		// 语句块内部按照语句类型再进行执行
		rt.PushBlockType(this.blockType);
		// 返回的结果
		LuneObject res = null;
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
				if(!rt.isInBlock(BlockStatementType.FUNCTION_BLOCK))
					throw new RuntimeException();
				res = state.OnExecute(rt, object);
				// 设置RT进入跳出运行 必须跳出直到遇到函数语句块则结束
				rt.IsReturnFlag = true;
				break;
			}
			// 循环的break
			else if(state.statementType == StatementType.BREAK)
			{
				if(!rt.isInBlock(BlockStatementType.LOOP_BLOCK))
					throw new RuntimeException();
				rt.IsBreakFlag = true;
				break;
			}
			// 循环的continue
			else if(state.statementType == StatementType.CONTINUE)
			{
				if(!rt.isInBlock(BlockStatementType.LOOP_BLOCK))
					throw new RuntimeException();
				rt.IsContinueFlag = true;
				break;
			}
//			if(res != null)
//				System.out.println(res.toString());
		}
		// 弹出类型
		rt.PopBlockType();
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
