package org.jeff.lune.parsers;

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
	public BlockStatementType blockType;
	/**
	 * 语句块
	 */
	public BlockStatement(BlockStatementType bType)
	{
		this.blockType = bType;
		this.statementType = StatementType.BLOCK;
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
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		rt.PushBlockType(this.blockType);
		LuneObject res = null;
		for(Statement state: this.body)
		{
			if(rt.IsBreakFlag || rt.IsContinueFlag || rt.IsReturnFlag)
				break;
			if(state.statementType == StatementType.ASSIGNMENT)
				state.OnExecute(rt, object);
			else if(state.statementType == StatementType.MEMBER)
				state.OnExecute(rt, object);
			else if(state.statementType == StatementType.IF)
				state.OnExecute(rt, object);
			else if(state.statementType == StatementType.WHILE)
				state.OnExecute(rt, object);
			else if(state.statementType == StatementType.FOR)
				state.OnExecute(rt, object);
			else if(state.statementType == StatementType.INDEX)
				state.OnExecute(rt, object);
			else if(state.statementType == StatementType.FUNCCALL)
				state.OnExecute(rt, object);
			else if(state.statementType == StatementType.EXPRESSION_BINARY)
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
		rt.PopBlockType();
		return res;
	}
}
