package org.jeff.lune.parsers;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.parsers.exps.AssignmentExpression;
import org.jeff.lune.parsers.exps.BinaryExpression;
import org.jeff.lune.parsers.exps.CallExpression;
import org.jeff.lune.parsers.exps.FunctionExpression;
import org.jeff.lune.parsers.exps.IndexExpression;
import org.jeff.lune.parsers.exps.ListExpression;
import org.jeff.lune.parsers.exps.MapExpression;
import org.jeff.lune.parsers.exps.MemberExpression;
import org.jeff.lune.parsers.exps.ParamsExpression;
import org.jeff.lune.parsers.exps.UnaryExpression;
import org.jeff.lune.parsers.objs.IdentifierStatement;
import org.jeff.lune.parsers.objs.NumberStatement;
import org.jeff.lune.parsers.objs.StringStatement;
import org.jeff.lune.token.Token;
import org.jeff.lune.token.TokenReader;
import org.jeff.lune.token.TokenType;

/**
 * 语句类型：
 *  1. 语句块 BlockStatement 
 *  2. 表达式语句 ExpressionStatement
 *  	2.1. 赋值表达式  AssignmentExpression
 *      2.2. 调用表达式 CallExpression
 *      2.3. 属性访问表达式 MemberExpression
 *      2.4. 函数表达式 FunctionExpression 
 *      2.5. This表达式 ThisExpression
 *      2.6 二元表达式 BinaryExpression
 *      2.7 一元表达式 UnaryExpression
 *  2. IFElse语句 IfStatement
 *  3. For语句 ForStatement
 *  4. ForIn语句 ForInStatement
 *  5. While语句 WhileStatement
 *  6. Break语句 BreakStatement
 *  7. Continue语句 ContinueStatement
 *  8. Return语句 ReturnStatement
 * @author qingf
 *
 */
public class SyntaxParser
{
	List<Token> tokens_;
	int tokenIndex_;
	int tokenSize_;
	List<Statement> program_statements = new LinkedList<Statement>();
	TokenReader tokenReader_;
	String file_;
	ProgramStatement program_;
	public ProgramStatement GetProgram()
	{
		return program_;
	}
	public SyntaxParser(Reader reader, String filename)
	{
		this.tokenReader_ = new TokenReader(reader, filename);
		this.file_ = filename;
	}
	
	public void parser()
	{
		this.tokens_ = this.tokenReader_.tokenizer();
		this.tokenIndex_ = 0;
		this.tokenSize_ = this.tokens_.size();
		this.program_parser();
	}
	
	void program_parser()
	{
		program_ = new ProgramStatement(this.file_);
		do
		{
			Statement state = this.statement_parser(false);
			if(state == null) break;
			program_.AddStatement(state);
		}while(true);
		//System.out.print(program_.toString());
	}

	static final String SYNTAX_ERROR = "syntax error , unexpceted `%s`, line:%s, column:%s";
	BlockStatement block_parser()
	{
		BlockStatement block = new BlockStatement();
		do
		{
			Statement state = this.statement_parser(false);
			if(state == null) break;
			block.AddStatement(state);
		}while(true);
		return block;
	}
	FunctionExpression function_parser(FunctionExpression function)
	{
		// 函数仅支持 xx.x.xx = function()
		Token next_token = this.GetToken();
		if(next_token.tokenType != TokenType.LPAREN)
		{
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		}
		ParamsExpression params = this.function_params_parser();
		function.params = params;
		next_token = this.GetToken();
		if(next_token.tokenType != TokenType.LCURLY)
		{
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		}
		BlockStatement body = this.block_parser();
		function.body = body;
		// TODO
		next_token = this.GetToken();
		if(next_token == null || next_token.tokenType != TokenType.RCURLY)
		{
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		}
		
		return function;
	}
	
	/**
	 * 函数的参数，只能是简单的变量
	 * @return
	 */
	ParamsExpression function_params_parser()
	{
		ParamsExpression params = new ParamsExpression();
		do
		{
			Token token = this.GetToken();
			if(token.tokenType == TokenType.RPAREN )
				break;
			if(token.tokenType == TokenType.IDENTIFIER)
			{
				IdentifierStatement idt = new IdentifierStatement(token);
				params.AddParam(idt);
			}
			token = this.GetToken();
			if(token.tokenType == TokenType.COMMA )  //
			{
				continue;
			}else if(token.tokenType == TokenType.RPAREN)
			{
				break;
			}else
			{
				throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
			}
		}while(true);
		return params;
	}
	
	ForLoopStatement for_loop_parser(Token token)
	{
		ForLoopStatement for_state = new ForLoopStatement();
		return for_state;
	}
	
	WhileStatement while_loop_parser(Token token)
	{
		WhileStatement while_state = new WhileStatement();
		return while_state;
	}
	
	IfElseStatement ifelse_parser(Token token)
	{
		IfElseStatement if_state = new IfElseStatement();
		return if_state;
	}
	Statement statement_parser(boolean onlyLeft)
	{
		Statement left = null;
		while(true)
		{
			while(left == null)
			{
				Token token = this.GetToken();
				if(token == null)
					return left;
				if(token.tokenType == TokenType.RBRACK || 
						 token.tokenType == TokenType.RCURLY ||
						 token.tokenType == TokenType.RPAREN ||
						 token.tokenType == TokenType.COMMA ||
						 token.tokenType == TokenType.COLON)
				{
					this.PutToken();
					break;
				}
				if(token.tokenType== TokenType.SEMICOLON)
				{
					break;
				}
				
				// 变量
				if(token.tokenType == TokenType.IDENTIFIER)
				{
					left = new IdentifierStatement(token);
					break;
				}// 字符串
				else if(token.tokenType == TokenType.STRING)
				{
					left = new StringStatement(token);
					break;
				}// 数字
				else if(token.tokenType == TokenType.NUMBER || token.tokenType ==  TokenType.HEXNUMBER)
				{
					left = new NumberStatement(token); 
					break;
				}// 列表
				else if(token.tokenType == TokenType.LBRACK)  //[
				{
					ListExpression nlist = new ListExpression();
					do
					{
						// 解析每个元素
						Statement element = this.statement_parser(true);
						if(element != null)
						{
							nlist.AddElement(element);
						}
						
						// 看下一个元素是,还是]
						Token next_token = this.GetToken();
						if(next_token.tokenType == TokenType.COMMA)
						{
							continue;
						}else if(next_token.tokenType == TokenType.RBRACK)
						{
							break;
						}
					}while(true);
					// 列表不可能存在于表达式的左侧。
					return nlist;
				}// 字典
				else if(token.tokenType == TokenType.LCURLY) //{
				{
					MapExpression ndict = new MapExpression();
					do
					{
							// 先获取key
							Statement key = this.statement_parser(true);
							if(key != null)
							{
								Token next_token = this.GetToken();
								if(next_token == null )
								{
									throw new RuntimeException(String.format(SYNTAX_ERROR, key.toString(), key.startLine, key.startColoumn));
								}
								if(next_token.tokenType != TokenType.COLON)
								{
									throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
								}
								// 获取值
								Statement value = this.statement_parser(false);
								if(value == null) //不可没有值
								{
									throw new RuntimeException(String.format(SYNTAX_ERROR, key.toString(), key.startLine, key.startColoumn));
								}
								ndict.Add(key, value);
							}
							// 继续解析下一个
							Token next_token = this.GetToken();
							if(next_token == null )
							{
								throw new RuntimeException(String.format(SYNTAX_ERROR, key.toString(), key.startLine, key.startColoumn));
							}
							if(next_token.tokenType == TokenType.COLON)
								continue;
							else if(next_token.tokenType == TokenType.RCURLY)
							{
								break;
							}
						}while(true);
					// 字典也不可能是左侧数据
					return ndict;
				}
				// (
				else if(token.tokenType == TokenType.LPAREN)
				{
					left = this.statement_parser(false);
					Token next_token = this.GetToken();
					if(next_token == null )
					{
						throw new RuntimeException(String.format(SYNTAX_ERROR, left.toString(), left.startLine, left.startColoumn));
					}
					else if(next_token.tokenType != TokenType.RPAREN)
					{
						throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
					}
					left.isWhole = true;
				}
				// 取not
				else if(token.tokenType == TokenType.OP_NOT)
				{
					Statement value = this.statement_parser(true);
					if(value == null)
						throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
					left = new UnaryExpression(TokenType.OP_NOT, value);
				}// 取反
				else if(token.tokenType == TokenType.OP_BIT_XOR)
				{
					Statement value = this.statement_parser(true);
					if(value == null)
						throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
					left = new UnaryExpression(TokenType.OP_NOT, value);
				}// 函数
				else if(token.tokenType == TokenType.KW_FUNCTION)
				{
					FunctionExpression func = new FunctionExpression();
					this.function_parser(func);
					// 函数也不会在左侧
					return func;
				}else if(token.tokenType == TokenType.KW_IF)
				{
					
				}else if(token.tokenType == TokenType.KW_ELIF)
				{
					
				}else if(token.tokenType == TokenType.KW_ELSE)
				{
					
				}else if(token.tokenType == TokenType.KW_FOR)
				{
					
				}else if(token.tokenType == TokenType.KW_WHILE)
				{
					
				}else if(token.tokenType ==TokenType.KW_BREAK)
				{
					
				}else if(token.tokenType == TokenType.KW_CONTINUE)
				{
					
				}else if(token.tokenType == TokenType.KW_RETURN)
				{
					ReturnStatement ret = new ReturnStatement();
					Statement exp = this.statement_parser(false);
					if(exp == null)
						throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
					ret.expression = exp;
					left = ret;
					// return也不会在左侧
					return left;
				}
				else
				{
					switch(token.tokenType)
					{
						case RBRACK:
						case RCURLY:
						case RPAREN:
						case COMMA:
						case COLON:
						case OP_DOT:
							this.PutToken();
						case SEMICOLON:
							break;
						default:
							throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
					}
				}
			} // ==== left end
			
			Token next_token = this.GetToken();
			if(next_token == null)
				return left;
			// ( 函数调用
			if(next_token.tokenType == TokenType.LPAREN )
			{
				CallExpression ncall = new CallExpression();
				ncall.variable = left;
				
				ParamsExpression params = new ParamsExpression();
				do
				{
					Statement node = this.statement_parser(false);
					if(node == null) break;
					params.AddParam(node);
					Token next_tk = this.GetToken();
					if(next_tk == null)
					{
						throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
					}
					if(next_tk.tokenType == TokenType.COMMA)
					{
						continue;
					}
					if(next_tk.tokenType == TokenType.RPAREN)
					{
						break;
					}
				}while(true);
				ncall.params = params;
				left = ncall;
				break;
			} // 字典取值 或者列表索引
			else if(next_token.tokenType == TokenType.LBRACK) //[
			{
				IndexExpression meb = new IndexExpression();
				meb.object = left;
				
				Statement child = this.statement_parser(false);
				if(child == null)
				{
					throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
				}
				Token next = this.GetToken();
				if(next == null)
				{
					throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
				}
				if(next.tokenType != TokenType.RBRACK)
				{
					throw new RuntimeException(String.format(SYNTAX_ERROR, next.tokenStr, next.tokenLine, next.tokenCol));
				}
				meb.index = child;
				left = meb;
				continue;
			}// 赋值语句
			else if(next_token.tokenType == TokenType.OP_ASSSIGN)
			{
				AssignmentExpression assign = new AssignmentExpression();
				assign.variable =left;
				
				Statement value = this.statement_parser(false);
				if(value == null)
					throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
				assign.value = value;
				left = assign;
				break;
			}// 属性索引 dot
			else if(next_token.tokenType == TokenType.OP_DOT)
			{
				MemberExpression meb = new MemberExpression();
				meb.parent = left;
				Statement child = this.statement_parser(true);
				if(child == null)
				{
					throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
				}
				meb.child = child;
				left = meb;
				break;
			}
			if(onlyLeft)
			{
				this.PutToken();
				 return left;
			}
			// 所以二进制的操作
			BinaryExpression binExp = new BinaryExpression(next_token.tokenType, next_token.tokenStr);
			Statement right = null;
			switch(next_token.tokenType)
			{
				case OP_PLUS:
				case OP_MINUS:
				case OP_MULTI:
				case OP_DIV:
				case OP_MOD:
				case OP_AND:
				case OP_OR:
				case OP_EQ:
				case OP_LE:
				case OP_LT:
				case OP_GE:
				case OP_GT:
				case OP_BIT_AND:
				case OP_BIT_OR:
				case OP_BIT_LEFT:
				case OP_BIT_RIGHT:
					right = this.statement_parser(true);
					if(right == null)
						throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
					binExp.left = left;
					binExp.right = right;
					left = ExpressionXX(binExp);
					break;
				default:
					this.PutToken();
					return left;
			}
		}
		return left;
	}
	
	BinaryExpression ExpressionXX(BinaryExpression bin)
	{
		Statement left = bin.left;
		if(left.statementType == StatementType.EXPRESSION_BINARY && left.isWhole == false)
		{
			BinaryExpression l = (BinaryExpression)left;
			
			if(bin.opValue <  l.opValue)  // 优先级
			{
				BinaryExpression new_left = new BinaryExpression(l.opType, l.op);
				new_left.left = l.left;
				new_left.right = bin;
				bin.left = l.right;
				if(bin.right.statementType == StatementType.EXPRESSION_BINARY)
				{
					bin.right = ExpressionXX((BinaryExpression) bin.right);
				}
				return new_left;
			}
		}
		return bin;
	}
	
	public Token GetToken()
	{
		if(tokenIndex_ < tokenSize_)
		{
			return tokens_.get(tokenIndex_++);
		}
		return null;
	}
	
	public void PutToken()
	{
		tokenIndex_ -= 1;
	}
}
