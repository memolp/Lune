package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

/**
 * 程序语句块，一般是整个文件的语句模块
 * @author 覃贵锋
 *
 */
public class ProgramStatement extends BlockStatement 
{
	/** 记录当前属于哪个文件 */
	public String sourceFile = null;
	/**
	 * 程序语句块，没有其他的功能
	 */
	public ProgramStatement(String srcFile, int line, int col)
	{
		super(line, col);
		this.sourceFile = srcFile;
	}
	/**
	 * 程序代码语句块运行执行内部的block
	 */
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object)
	{
		Statement.CurrentFile = this.sourceFile;
		// 压入代码块类型
		rt.PushBlockType(BlockStatementType.PROGRAM_BLOCK);
		// 执行内部语句 = 得到结果
		LuneObject res = super.OnExecute(rt, object);
		// 弹出代码类型
		rt.PopBlockType();
		// 返回结果
		return res;
	}
}
