package org.jeff.lune.token;

import java.io.IOException;
import java.io.Reader;

/**
 * Token读取器
 * @author 覃贵锋
 *
 */
public class TokenReader
{
	final static int EOF = -1;		// EOF
	final static int ENTER = '\n';
	final static int CR = '\r';
	final static int TAB = '\t';
	final static int WITESPACE = ' ';
	final static int DOT = '.';
	final static int UNDER = '_';
	/**
	 * 数字
	 * @param c
	 * @return
	 */
	final static boolean isNumber(int c)
	{
		if (c >= '0' && c <= '9')  return true;
		return false;
	}
	/**
	 * 字母
	 * @param c
	 * @return
	 */
	final static boolean isAlpha(int c)
	{
		if(c >= 'a' && c <= 'z' ) return true;
		if(c >= 'A' && c <= 'Z') return true;
		return false;
	}
	/**
	 * 操作符
	 * @param c
	 * @return
	 */
	final static boolean isOperator(int c)
	{
		if(c == '+' || c =='-' || c =='*' || c =='/' || c =='%' )
			return true;
		return false;
	}
	/**
	 * 可能由两个组成一个操作
	 * @param c
	 * @return
	 */
	final static boolean IsMaybeTwoOperator(int c)
	{
		if(c == '>' || c == '<' || c == '=' || c =='!' )
			return true;
		return false;
	}
	/**
	 * 字符串
	 * @param c
	 * @return
	 */
	final static boolean isStringTag(int c)
	{
		if(c =='"' || c == '\'')
			return true;
		return false;
	}
	/**
	 * 是否是空白字符
	 * @param c
	 * @return
	 */
	final static boolean isWitespace(int c)
	{
		if(c == ' ' || c =='\r' || c =='\t')
			return true;
		return false;
	}
	
	static final String UNEXPECTEDSYMBOL = "unexpected symbol `%c` in file:%s, line:%s, column:%s";
	/** 当前的读取器 需要支持mark和reset */
	Reader reader_;
	StringBuilder current_sb_;
	TokenType current_type_;
	int lineno_;
	int column_;
	String filename_;
	/**
	 * Token读取器
	 * @param reader 需要支持mark和reset
	 * @param filename
	 */
	public TokenReader(Reader reader, String filename)
	{
		lineno_ = 1;
		column_ = 0;
		reader_ = reader;
		filename_ = filename;
	}
	/**
	 * 解析数字 支持整数、浮点数和十六进制（科学计数法暂时先不支持）
	 * @param c
	 * @return
	 */
	Token ParseNumber(int c)
	{
		current_type_ = TokenType.NUMBER;	// 默认先设置为整数
		AppendChar(c);
		int cache_lineno = lineno_;
		int cache_col = column_;
		
		boolean isFloat = c == '.';
		boolean isHex = false;
		do
		{
			int b = GetChar();
			if(b == EOF) break;
			if(isWitespace(b)) break;
			if(b == ENTER) // 特殊对换行处理，要增加行号
			{
				lineno_++;
				column_ = 0;
				break;
			}
			// 数字直接加
			if(isNumber(b))
			{
				AppendChar(b);
				continue;
			}
			// 处理十六进制
			else if(b =='x' || b =='X')
			{
				if(current_sb_.length() == 1 && current_sb_.charAt(0) == '0')
				{
					isHex = true;
					AppendChar(b);
					continue;
				}else
				{
					throw new RuntimeException(String.format(UNEXPECTEDSYMBOL, b, filename_, lineno_, column_));
				}
			}
			// 小数
			else if(b == '.') 
			{
					if(isFloat == false)
					{
						isFloat = true;
						AppendChar(b);
						continue;
					}else
					{
						throw new RuntimeException(String.format(UNEXPECTEDSYMBOL, b, filename_, lineno_, column_));
					}
			}
			// 这里需要判断十六进制
			else if((b >= 'A' && b <= 'F') || (b >= 'a' && b <= 'f'))
			{
				if(isHex) // 如果不是十六进制就是语法错误
				{
					AppendChar(b);
					continue;
				}else
				{
					throw new RuntimeException(String.format(UNEXPECTEDSYMBOL, b, filename_, lineno_, column_));
				}
			}
			else 
			{
				PutChar();
				break;
			}
		}while(true);
		// 返回Token
		return new Token(current_type_, current_sb_.toString(), cache_lineno, cache_col);
	}
	/**
	 * 解析变量标识符 仅支持_ 和 字母，数字（数字不可打头）
	 * @param c
	 * @return
	 */
	Token ParseIdentify(int c)
	{
		current_type_ = TokenType.IDENTIFIER;
		AppendChar(c);
		int cache_lineno = lineno_;
		int cache_col = column_;
		do
		{
			int b = GetChar();
			if(b == EOF) break;
			if(isWitespace(b)) break;
			if(b == '\n')
			{
				lineno_++;
				column_ = 0;
				break;
			}
			if(isAlpha(b))
			{
				AppendChar(b);
				continue;
			}else if(b == UNDER)
			{
				AppendChar(b);
				continue;
			}else if(isNumber(b))
			{
				AppendChar(b);
				continue;
			}else
			{
				PutChar();
				break;
			}
		}while(true);
		// 处理全部关键字
		String identify = current_sb_.toString();
		if(identify.equals("function"))
		{
			return new Token(TokenType.KW_FUNCTION, identify, cache_lineno, cache_col);
		}else if(identify.equals("if"))
		{
			return new Token(TokenType.KW_IF, identify, cache_lineno, cache_col);
		}else if(identify.equals("elif"))
		{
			return new Token(TokenType.KW_ELIF, identify, cache_lineno, cache_col);
		}else if(identify.equals("else"))
		{
			return new Token(TokenType.KW_ELSE, identify, cache_lineno, cache_col);
		}else if(identify.equals("for"))
		{
			return new Token(TokenType.KW_FOR, identify, cache_lineno, cache_col);
		}else if(identify.equals("in"))
		{
			return new Token(TokenType.KW_IN, identify, cache_lineno, cache_col);
		}else if(identify.equals("while"))
		{
			return new Token(TokenType.KW_WHILE, identify, cache_lineno, cache_col);
		}else if(identify.equals("break"))
		{
			return new Token(TokenType.KW_BREAK, identify, cache_lineno, cache_col);
		}else if(identify.equals("continue"))
		{
			return new Token(TokenType.KW_CONTINUE, identify, cache_lineno, cache_col);
		}else if(identify.equals("return"))
		{
			return new Token(TokenType.KW_RETURN, identify, cache_lineno, cache_col);
		}else if(identify.equals("true"))
		{
			return new Token(TokenType.KW_TRUE, identify, cache_lineno, cache_col);
		}
		else if(identify.equals("false"))
		{
			return new Token(TokenType.KW_FALSE, identify, cache_lineno, cache_col);
		}
		// 返回Token
		return new Token(current_type_, identify, cache_lineno, cache_col);
	}
	/**
	 * 解析字符串（支持单双引号）
	 * @param c
	 * @return
	 */
	Token ParseString(int c)
	{
		//AppendChar(c);
		current_type_ = TokenType.STRING;
		int cache_lineno = lineno_;
		int cache_col = column_;
		do
		{
			int b = GetChar();
			if(b == EOF)
			{
				throw new RuntimeException(String.format(UNEXPECTEDSYMBOL, c, filename_, lineno_, column_));
			}
			if(b == '\n')
			{
				if(current_sb_.charAt(current_sb_.length()-1) == '\\')  //末尾加 `\`后可以换行
				{
					lineno_++;
					column_ = 0;
					continue;
				}
				throw new RuntimeException(String.format(UNEXPECTEDSYMBOL, b, filename_, lineno_, column_));
			}
			// 处理转义情况
			if(b == '\\')
			{
				int next = GetChar();
				if(next == EOF)
				{
					throw new RuntimeException();
				}
				AppendChar(next);
				continue;
			}
			if(b == c) // '' or ""
			{
				//AppendChar(b);
				break;
			}
			AppendChar(b);
		}while(true);
		// 返回Token
		return new Token(current_type_, current_sb_.toString(), cache_lineno, cache_col);
	}
	/**
	 * 注释 仅支持#开始的单行注释
	 * @param c
	 * @return
	 */
	Token ParseComment(int c)
	{
		AppendChar(c);
		current_type_ = TokenType.COMMENT;
		int cache_lineno = lineno_;
		int cache_col = column_;
		do
		{
			int b = GetChar();
			if(b == EOF )
			{
				break;
			}else if(b == '\n')
			{
				lineno_ ++;
				column_ = 0;
				break;
			}
			AppendChar(b);
		}while(true);
		// 返回Token
		return new Token(current_type_, current_sb_.toString(), cache_lineno, cache_col);
	}
	/**
	 * 获取一个字符-并标记，可以reset
	 * @return
	 */
	int GetChar()
	{
		try
		{
			reader_.mark(1);
			int b = reader_.read();
			column_++;
			return b;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * 放回去一个字符
	 */
	void PutChar()
	{
		try
		{
			reader_.reset();
			column_--;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 将字符添加到StringBuilder中
	 * @param code
	 */
	void AppendChar(int code)
	{
		char c = (char)code;
		current_sb_.append(c);
	}
	/**
	 * 获取Token，内部处理区分字符串，数字，标识符等
	 * @return
	 */
	public Token GetToken()
	{
		current_sb_ = new StringBuilder();
		current_type_ = TokenType.UNKOWN;
		for(;;)
		{
			int b = GetChar();
			if(b == EOF)
			{
				if(current_sb_.length() > 0)
				{
					throw new RuntimeException("???????????");
				}
				return null;
			}
			if(isWitespace(b)) continue;
			if(b == '\n')
			{
				lineno_ ++;
				column_ = 0;
				continue;
			}
			if(isNumber(b))
			{
				return this.ParseNumber(b);
			}
			if(isAlpha(b) || b == UNDER)
			{
				return this.ParseIdentify(b);
			}
			if(isStringTag(b))
			{
				return this.ParseString(b);
			}
			if(b == '#')
			{
				return this.ParseComment(b);
			}
			// 点号运算符，可能是小数点，可能数属性访问
			if(b == '.')
			{
				int n = GetChar();
				PutChar();
				if(isNumber(n))
				{
					return this.ParseNumber(b);
				}else
				{
					return new Token(TokenType.OP_DOT, ".", lineno_, column_);
				}
			}
			// 大于号运算符，可能是> 、>=、>> 三种情况
			if(b == '>')
			{
				int n = GetChar();
				if(n == '>')
				{
					return new Token(TokenType.OP_BIT_RIGHT, ">>", lineno_, column_);
				}else if(n == '=')
				{
					return new Token(TokenType.OP_GE, ">=", lineno_, column_);
				}else
				{
					PutChar();
					return new Token(TokenType.OP_GT, ">", lineno_, column_);
				}
			}
			// 小于号运算符，可能是<、<=、<<
			if(b == '<')
			{
				int n = GetChar();
				if(n == '<')
				{
					return new Token(TokenType.OP_BIT_LEFT, "<<", lineno_, column_);
				}else if(n == '=')
				{
					return new Token(TokenType.OP_LE, "<=", lineno_, column_);
				}else
				{
					PutChar();
					return new Token(TokenType.OP_LT, "<", lineno_, column_);
				}
			}
			// 所有的操作符判断
			switch(b)  
			{
				case '+':  // 支持 += 操作
					if(GetChar() == '=')
					{
						return new Token(TokenType.OP_PLUS_ASSIGN, "+=", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_PLUS, "+", lineno_, column_);
				case '-':  // 支持 -= 操作
					if(GetChar() == '=')
					{
						return new Token(TokenType.OP_MINUS_ASSIGN, "-=", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_MINUS, "-", lineno_, column_);
				case '*':
					if(GetChar() == '=')
					{
						return new Token(TokenType.OP_MULTI_ASSIGN, "*=", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_MULTI, "*", lineno_, column_);
				case '/':
					if(GetChar() == '=')
					{
						return new Token(TokenType.OP_DIV_ASSIGN, "/=", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_DIV, "/", lineno_, column_);
				case '%':
					if(GetChar() == '=')
					{
						return new Token(TokenType.OP_MOD_ASSIGN, "%=", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_MOD, "%", lineno_, column_);
				case '=':
					if(GetChar() == '=')
					{
						return new Token(TokenType.OP_EQ, "==", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_ASSIGN, "=", lineno_, column_);
				case '!':
					if(GetChar() == '=')
					{
						return new Token(TokenType.OP_NE, "!=", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_NOT, "!", lineno_, column_);
				case '&':
					if(GetChar() == '&')
					{
						return new Token(TokenType.OP_AND, "&&", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_BIT_AND, "&", lineno_, column_);
				case '|':
					if(GetChar() == '|')
					{
						return new Token(TokenType.OP_OR, "||", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_BIT_OR, "|", lineno_, column_);
				case '^':
					return new Token(TokenType.OP_BIT_XOR, "^", lineno_, column_);
				case '~':
					return new Token(TokenType.OP_BIT_NOT, "~", lineno_, column_);
				case '[':
					return new Token(TokenType.LBRACK, "[", lineno_, column_);
				case ']':
					return new Token(TokenType.RBRACK, "]", lineno_, column_);
				case '(':
					return new Token(TokenType.LPAREN, "(", lineno_, column_);
				case ')':
					return new Token(TokenType.RPAREN, ")", lineno_, column_);
				case '{':
					return new Token(TokenType.LCURLY, "{", lineno_, column_);
				case '}':
					return new Token(TokenType.RCURLY, "}", lineno_, column_);
				case ';':
					return new Token(TokenType.SEMICOLON, ";", lineno_, column_);
				case ':':
					return new Token(TokenType.COLON, ":", lineno_, column_);
				case ',':
					return new Token(TokenType.COMMA, ",", lineno_, column_);
			}
			throw new RuntimeException(String.format(UNEXPECTEDSYMBOL, b, filename_, lineno_, column_));
		}
	}
}
