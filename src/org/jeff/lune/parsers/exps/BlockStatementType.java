package org.jeff.lune.parsers.exps;

/**
 * 语句块所属的类型定义
 * @author 覃贵锋
 *
 */
public enum BlockStatementType 
{
	/** 程序，文件语句块 --产生命名空间 */
	PROGRAM_BLOCK,
	/** 函数内部语句块 - 产生命名空间 */
	FUNCTION_BLOCK,
	/** 条件分支语句块 */
	IFELSE_BLOCK,
	/** 循环迭代器语句块 */
	LOOP_BLOCK
}
