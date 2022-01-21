package org.jeff.lune.parsers.exps;

/**
 * 语句类型
 * @author 覃贵锋
 *
 */
public enum StatementType
{
	/** 标识符，变量名称 */
	IDENTIFIER,
	/** 数字表达式语句- 整数、小数、十六进制 */
	NUMBER,	
	/** 布尔表达式 true和false */
	BOOLEAN,
	/** None */
	NONE,
	/** 字符串表达式 */
	STRING,
	/**  赋值语句 */
	ASSIGNMENT,
	/** 函数语句 */
	FUNCTION,		
	/** 函数调用 */
	FUNCCALL,
	/** 属性访问 a.b */
	MEMBER,	
	/** 下标访问 a[1] a['x'] */
	INDEX,	
	/**  二元表达式 */
	EXPRESSION_BINARY,	
	/** 一元表达式 */
	EXPRESSION_UNARY, 
	/** 列表 */
	LIST_OBJECT,
	/** 字典 */
	MAP_OBJECT, 
	
	/**  if表达式 */
	IF,						
	/** elif表达式  */
	ELIF,					
	/** else表达式 */
	ELSE,				
	/** while 循环 */
	WHILE,		
	/** for 迭代器  */
	FOR,		
	/** 语句块 -用于函数体、各种控制语句体、文件代码体 */
	BLOCK,
	/** return 函数内 */
	RETURN,		
	/** break 循环内，迭代器内 */
	BREAK,
	/** continue循环内， 迭代器内 */
	CONTINUE,
}
