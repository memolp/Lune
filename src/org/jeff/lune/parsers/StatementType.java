package org.jeff.lune.parsers;

/**
 * 语句类型
 * @author qingf
 *
 */
public enum StatementType
{
	IDENTIFIER,			// 标识符，所有语句的最终节点
	ASSIGNMENT,		// 赋值语句
	PARAMS,				// 参数
	FUNCTION,			// 函数
	FUNCCALL,			// 函数调用
	EXPRESSION,		// 表达式
	IF,							// if
	BLOCK,				// 语句块

	
}
