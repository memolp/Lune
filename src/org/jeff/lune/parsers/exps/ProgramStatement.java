package org.jeff.lune.parsers.exps;


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
		super(BlockStatementType.PROGRAM_BLOCK, line, col);
		this.sourceFile = srcFile;
	}
}
