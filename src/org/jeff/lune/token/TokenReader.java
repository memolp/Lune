package org.jeff.lune.token;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
	
	final static boolean isWitespace(int c)
	{
		if(c == ' ' || c =='\r' || c =='\t')
			return true;
		return false;
	}
	
	static final String UNEXPECTEDSYMBOL = "unexpected symbol `%c` in file:%s, line:%s, column:%s";

	Reader reader_;
	StringBuilder current_sb_;
	TokenType current_type_;
	int lineno_;
	int column_;
	String filename_;
	
	public TokenReader(Reader reader, String filename)
	{
		lineno_ = 1;
		column_ = 1;
		reader_ = reader;
		filename_ = filename;
	}
	
	Token ParseNumber(int c)
	{
		current_type_ = TokenType.NUMBER;
		AppendChar(c);
		int cache_lineno = lineno_;
		int cache_col = column_;
		
		boolean isFloat = c == '.';
		do
		{
			int b = GetChar();
			if(b == EOF) break;
			if(isWitespace(b)) break;
			if(b == ENTER) // 特殊对换行处理，要增加行号
			{
				lineno_++;
				column_ = 1;
				break;
			}
			if(isNumber(b))
			{
				AppendChar(b);
				continue;
			}else if(b =='x' || b =='X')
			{
				if(current_sb_.length() == 1 && current_sb_.charAt(0) == '0')
				{
					current_type_ = TokenType.HEXNUMBER;
					AppendChar(b);
					continue;
				}else
				{
					throw new RuntimeException(String.format(UNEXPECTEDSYMBOL, b, filename_, lineno_, column_));
				}
			}else if(b == '.')  // 小数
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
			}else 
			{
				PutChar();
				break;
			}
		}while(true);
		// 返回Token
		return new Token(current_type_, current_sb_.toString(), cache_lineno, cache_col);
		
	}
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
				column_ = 1;
				break;
			}
			if(isAlpha(b))
			{
				AppendChar(b);
				continue;
			}else if(b == '_')
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
		// 返回Token
		return new Token(current_type_, current_sb_.toString(), cache_lineno, cache_col);
	}
	
	Token ParseString(int c)
	{
		AppendChar(c);
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
					column_ = 1;
					continue;
				}
				throw new RuntimeException(String.format(UNEXPECTEDSYMBOL, b, filename_, lineno_, column_));
			}
			if(b == c) // '' or ""
			{
				// 处理 " \" " or ' \' '
				if(current_sb_.charAt(current_sb_.length()-1) == '\\')
				{
					AppendChar(b);
					continue;
				}else
				{
					//End b
					AppendChar(b);
					break;
				}
			}
			AppendChar(b);
		}while(true);
		// 返回Token
		return new Token(current_type_, current_sb_.toString(), cache_lineno, cache_col);
	}
	/**
	 * 注释
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
			if(b == EOF || b == '\n')
				break;
			AppendChar(b);
		}while(true);
		// 返回Token
		return new Token(current_type_, current_sb_.toString(), cache_lineno, cache_col);
	}
	
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
	void PutChar()
	{
		try
		{
			reader_.reset();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void AppendChar(int code)
	{
		char c = (char)code;
		current_sb_.append(c);
	}
	
	Token GetToken()
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
				column_ = 1;
				continue;
			}
			if(isNumber(b))
			{
				return this.ParseNumber(b);
			}
			if(isAlpha(b))
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
					// return DotTeken
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
						return new Token(TokenType.OP_PLUS, "+=", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_PLUS, "+", lineno_, column_);
				case '-':  // 支持 -= 操作
					if(GetChar() == '=')
					{
						return new Token(TokenType.OP_MINUS, "-=", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_MINUS, "-", lineno_, column_);
				case '*':
					return new Token(TokenType.OP_MULTI, "*", lineno_, column_);
				case '/':
					return new Token(TokenType.OP_DIV, "/", lineno_, column_);
				case '%':
					return new Token(TokenType.OP_MOD, "%", lineno_, column_);
				case '=':
					if(GetChar() == '=')
					{
						return new Token(TokenType.OP_EQ, "==", lineno_, column_);
					}
					PutChar();
					return new Token(TokenType.OP_ASSSIGN, "=", lineno_, column_);
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
				case ',':
					return new Token(TokenType.COLON, ",", lineno_, column_);
			}
			throw new RuntimeException(String.format(UNEXPECTEDSYMBOL, b, filename_, lineno_, column_));
		}
	}
	
	public List<Token> tokenizer()
	{
		List<Token> tokens = new ArrayList<Token>();
		Token temp = null;
		do
		{
			temp = GetToken();
			if(temp == null) break;
			tokens.add(temp);
		}while(true);
		return tokens;	
	}
}
