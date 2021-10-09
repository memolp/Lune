package org.jeff.lune.parsers;

/**
 * 语句类型
 * @author qingf
 *
 */
public enum StatementType
{
	IDENTIFIER,			// 标识符，所有语句的最终节点
	NUMBER,
	STRING,
	ASSIGNMENT,		// 赋值语句
	PARAMS,				// 参数
	FUNCTION,			// 函数
	FUNCCALL,			// 函数调用
	MEMBER,				// xx.x
	INDEX,					// xx["x"]  xx[1] xx[xx.x]
	EXPRESSION_BINARY,		// 二元表达式
	EXPRESSION_UNARY,  // 一元表达式
	LIST_OBJECT,	// 列表
	MAP_OBJECT,  // 字典
	IF,							// if
	BLOCK,				// 语句块
	RETURN,				// return

	
}
