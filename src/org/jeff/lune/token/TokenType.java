package org.jeff.lune.token;

public enum TokenType
{
	/**  error */
	UNKOWN,
	/** error */
	ERROR,
	/** 注释 */
	COMMENT,
	/** 标识符 */
	IDENTIFIER,
	/** 数字-统一用浮点数 */
	NUMBER,
	/** 字符串  */
	STRING,
	/** 布尔值 */
	BOOLEAN,
	/** 操作符 + */
	OP_PLUS,
	/** += 操作 */
	OP_PLUS_ASSIGN,
	/** 操作符 - */
	OP_MINUS,
	/** -= 操作 */
	OP_MINUS_ASSIGN,
	/** 操作符 * */
	OP_MULTI,
	/** *= 操作*/
	OP_MULTI_ASSIGN,
	/** 操作符 / */
	OP_DIV,
	/** /= 操作  */
	OP_DIV_ASSIGN,
	/** 操作符 % */
	OP_MOD,
	/** %= 操作 */
	OP_MOD_ASSIGN,
	/** 操作符 . */
	OP_DOT,
	/** 操作符 = */
	OP_ASSIGN,
	/** 操作符 == */
	OP_EQ,
	/** 操作符 != */
	OP_NE,
	/** 操作符 < */
	OP_LT,
	/** 操作符 <= */
	OP_LE,
	/** 操作符 > */
	OP_GT,
	/** 操作符 >= */
	OP_GE,
	/** 操作符 && */
	OP_AND,
	/** 操作符 || */
	OP_OR,
	/** 操作符 ! */
	OP_NOT,
	/** 操作符 & */
	OP_BIT_AND,
	/** 操作符 | */
	OP_BIT_OR,
	/** 操作符 ^ 异或 */
	OP_BIT_XOR,
	/** 操作符 ~ 取反 */
	OP_BIT_NOT,
	/** 操作符 >> */
	OP_BIT_RIGHT,
	/** 操作符 << */
	OP_BIT_LEFT,
	/** 操作符 [ */
	LBRACK,
	/** 操作符 ] */
	RBRACK,
	/** 操作符 ( */
	LPAREN,
	/** 操作符 ) */
	RPAREN,
	/** 操作符 { */
	LCURLY,
	/** 操作符 } */
	RCURLY,
	/** 操作符 : */
	COLON,
	/** 操作符 , */
	COMMA,
	/** 操作符 ; */
	SEMICOLON,
	/** 关键字 if */
	KW_IF,
	/** 关键字 elif */
	KW_ELIF,
	/** 关键字 else */
	KW_ELSE,
	/** 关键字 for */
	KW_FOR,
	/** 关键字 while */
	KW_WHILE,
	/** 关键字 break */
	KW_BREAK,
	/** 关键字 continue */
	KW_CONTINUE,
	/** 关键字 in */
	KW_IN,
	/** 关键字 true */
	KW_TRUE,
	/** 关键字 false */
	KW_FALSE,
	/** 关键字 return */
	KW_RETURN,
	/** 关键字 self */
	KW_SELF,
	/** 关键字 function */
	KW_FUNCTION,
	/** 关键字 var */
	KW_VAR,
	/** 关键字 ... */
	KW_ARGS,
	/** 关键字 None */
	KW_NONE,
}
