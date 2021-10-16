package org.jeff.lune.parsers;

import java.io.Reader;
import java.util.List;

import org.jeff.lune.parsers.exps.AssignmentExpression;
import org.jeff.lune.parsers.exps.BinaryExpression;
import org.jeff.lune.parsers.exps.BlockStatement;
import org.jeff.lune.parsers.exps.BlockStatementType;
import org.jeff.lune.parsers.exps.BreakStatement;
import org.jeff.lune.parsers.exps.CallExpression;
import org.jeff.lune.parsers.exps.ContinueStatement;
import org.jeff.lune.parsers.exps.ForLoopStatement;
import org.jeff.lune.parsers.exps.FunctionExpression;
import org.jeff.lune.parsers.exps.IfElseStatement;
import org.jeff.lune.parsers.exps.IndexExpression;
import org.jeff.lune.parsers.exps.MemberExpression;
import org.jeff.lune.parsers.exps.ProgramStatement;
import org.jeff.lune.parsers.exps.ReturnStatement;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;
import org.jeff.lune.parsers.exps.UnaryExpression;
import org.jeff.lune.parsers.exps.WhileStatement;
import org.jeff.lune.parsers.objs.BooleanStatement;
import org.jeff.lune.parsers.objs.IdentifierStatement;
import org.jeff.lune.parsers.objs.ListStatement;
import org.jeff.lune.parsers.objs.MapSatement;
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
 * @author 覃贵锋
 *
 */
public class SyntaxParser
{
	/** 当前文件的token列表-排除了注释的 */
	List<Token> tokens_;
	/** 当前索引的地址 */
	int tokenIndex_;
	/** 总tokens数量 */
	int tokenSize_;
	/** Token读取器 */
	TokenReader tokenReader_;
	/** 文件名称 */
	String file_;
	/** 当前文件的语句体 */
	ProgramStatement program_;
	/**
	 * 获取当前文件的语句体
	 * @return
	 */
	public ProgramStatement GetProgram()
	{
		return program_;
	}
	/**
	 * 创建语法解析器
	 * @param reader
	 * @param filename
	 */
	public SyntaxParser(Reader reader, String filename)
	{
		this.tokenReader_ = new TokenReader(reader, filename);
		this.file_ = filename;
	}
	/**
	 * 执行语法解析
	 */
	public void parser()
	{
		// 创建程序语句块
		program_ = new ProgramStatement(this.file_, -1, -1);
		this.block_parser(program_);
	}

	static final String SYNTAX_ERROR = "syntax error , unexpceted `%s`, line:%s, column:%s";
	/**
	 * 语句块解析
	 * @param block
	 */
	void block_parser(BlockStatement block)
	{
		do
		{
			Statement state = this.statement_parser(false);
			if(state == null) break;
			block.AddStatement(state);
		}while(true);
	}
	
	void function_parser(FunctionExpression function)
	{
		// 函数仅支持 xx.x.xx = function()
		Token next_token = this.GetToken();
		if(next_token.tokenType != TokenType.LPAREN)
		{
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		}
		this.function_params_parser(function.params);
		next_token = this.GetToken();
		if(next_token.tokenType != TokenType.LCURLY)
		{
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		}
		BlockStatement body = new BlockStatement(BlockStatementType.FUNCTION_BLOCK, next_token.tokenLine, next_token.tokenCol);
		this.block_parser(body);
		function.body = body;
		next_token = this.GetToken();
		if(next_token == null || next_token.tokenType != TokenType.RCURLY)
		{
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		}
	}
	/**
	 * 函数的参数，只能是简单的变量
	 * @return
	 */
	void function_params_parser(List<Statement> params)
	{
		do
		{
			Token token = this.GetToken();
			if(token.tokenType == TokenType.RPAREN )
				break;
			if(token.tokenType == TokenType.IDENTIFIER)
			{
				IdentifierStatement idt = new IdentifierStatement(token.tokenStr, token.tokenLine, token.tokenCol);
				params.add(idt);
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
	}
	/**
	 * for 迭代器解析
	 * @param for_state
	 */
	void for_loop_parser(ForLoopStatement for_state)
	{
		Token token = this.GetToken();
		if(token.tokenType  != TokenType.LPAREN)
			throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
		do
		{
			Token next_token = this.GetToken();
			if(next_token.tokenType ==TokenType.IDENTIFIER)
			{
				IdentifierStatement idt = new IdentifierStatement(next_token.tokenStr, next_token.tokenLine, next_token.tokenCol);
				for_state.params.add(idt);
			}else
			{
				throw new RuntimeException();
			}
			next_token = this.GetToken();
			if(next_token.tokenType == TokenType.COMMA)
				continue;
			if(next_token.tokenType == TokenType.KW_IN)
			{
				break;
			}
		}while(true);
		Statement iter = this.statement_parser(false);
		for_state.iterObject = iter;
		Token next_token = this.GetToken();
		if(next_token.tokenType != TokenType.RPAREN) throw new RuntimeException();
		next_token = this.GetToken();
		if(next_token.tokenType != TokenType.LCURLY) throw new RuntimeException();
		BlockStatement body = new BlockStatement(BlockStatementType.LOOP_BLOCK, next_token.tokenLine, next_token.tokenCol);
		this.block_parser(body);
		for_state.body = body;
		next_token = this.GetToken();
		if(next_token.tokenType != TokenType.RCURLY)
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
	}
	/**
	 * while解析
	 * @param wstate
	 */
	void  while_loop_parser(WhileStatement wstate)
	{
		Token token = this.GetToken();
		if(token.tokenType  != TokenType.LPAREN)
			throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
		Statement exp = this.statement_parser(false);
		wstate.condition = exp;
		Token next_token = this.GetToken();
		if(next_token.tokenType != TokenType.RPAREN)
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		next_token = this.GetToken();
		if(next_token.tokenType != TokenType.LCURLY)
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		BlockStatement body =  new BlockStatement(BlockStatementType.LOOP_BLOCK, next_token.tokenLine, next_token.tokenCol);
		this.block_parser(body);
		wstate.body = body;
		next_token = this.GetToken();
		if(next_token.tokenType != TokenType.RCURLY)
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
	}
	/**
	 * if分支条件解析
	 * @param if_state
	 * @param no_codition -- 这个只能是else才是true
	 */
	void ifelse_parser(IfElseStatement if_state, boolean no_codition)
	{
		if(!no_codition)
		{
			Token token = this.GetToken();
			if(token.tokenType  != TokenType.LPAREN)
				throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
			Statement exp = this.statement_parser(false);
			if_state.condition = exp;
			Token next_token = this.GetToken();
			if(next_token.tokenType != TokenType.RPAREN)
				throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		}
		Token next_token = this.GetToken();
		if(next_token.tokenType != TokenType.LCURLY)
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		BlockStatement body =  new BlockStatement(BlockStatementType.IFELSE_BLOCK, next_token.tokenLine, next_token.tokenCol);
		this.block_parser(body);
		if_state.body = body;
		next_token = this.GetToken();
		if(next_token.tokenType != TokenType.RCURLY)
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		next_token = this.GetToken();
		// 如果有elif分支
		if(next_token.tokenType ==TokenType.KW_ELIF)
		{
			IfElseStatement branch = new IfElseStatement(StatementType.ELIF, next_token.tokenLine, next_token.tokenCol);
			this.ifelse_parser(branch, false);
			if_state.Switch = branch;
		}
		else if(next_token.tokenType == TokenType.KW_ELSE)
		{
			IfElseStatement branch = new IfElseStatement(StatementType.ELSE, next_token.tokenLine, next_token.tokenCol);
			this.ifelse_parser(branch, true);
			if_state.Switch = branch;
		}
		else
		{
			this.PutToken();
		}
	}
	/**
	 * 语句表达式解析总的入口，内部进行各种类型的区分
	 * @param onlyLeft
	 * @return
	 */
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
				// 遇到] } ) ; : 这些都结束
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
					continue;
				}
				// 变量
				if(token.tokenType == TokenType.IDENTIFIER)
				{
					left = new IdentifierStatement(token.tokenStr, token.tokenLine, token.tokenCol);
					Token next_token = this.GetToken();
					// 需要针对属性访问做特殊处理
					if(next_token.tokenType == TokenType.OP_DOT)
					{
						MemberExpression meb = new MemberExpression(next_token.tokenLine, next_token.tokenCol);
						meb.parent = left;
						Statement child = this.statement_parser(true);
						if(child == null)
						{
							throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
						}
						meb.child = child;
						left = meb;
					}else
					{
						this.PutToken();
					}
					break;
				}// 字符串
				else if(token.tokenType == TokenType.STRING)
				{
					left = new StringStatement(token.tokenStr, token.tokenLine, token.tokenCol);
					break;
				}// 数字
				else if(token.tokenType == TokenType.NUMBER)
				{
					left = new NumberStatement(token.tokenStr, token.tokenLine, token.tokenCol); 
					break;
				}// 列表
				else if(token.tokenType == TokenType.LBRACK)  //[
				{
					ListStatement nlist = new ListStatement(token.tokenLine, token.tokenCol);
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
					MapSatement ndict = new MapSatement(token.tokenLine, token.tokenCol);
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
					left = new UnaryExpression(TokenType.OP_NOT, value, token.tokenLine, token.tokenCol);
				}// 取反
				else if(token.tokenType == TokenType.OP_BIT_XOR)
				{
					Statement value = this.statement_parser(true);
					if(value == null)
						throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
					left = new UnaryExpression(TokenType.OP_NOT, value, token.tokenLine, token.tokenCol);
				}// 取负数
				else if(token.tokenType == TokenType.OP_MINUS)
				{
					Token next_token = this.GetToken();
					if(next_token.tokenType != TokenType.NUMBER)
						throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
					NumberStatement num = new NumberStatement(next_token.tokenStr, next_token.tokenLine, next_token.tokenCol);
					num.value = -num.value;
					left = num;
					break;
				}// 函数
				else if(token.tokenType == TokenType.KW_FUNCTION)
				{
					FunctionExpression func = new FunctionExpression(token.tokenLine,  token.tokenCol);
					this.function_parser(func);
					// 函数也不会在左侧
					return func;
				}else if(token.tokenType == TokenType.KW_IF)
				{
					IfElseStatement ifstate = new IfElseStatement(StatementType.IF, token.tokenLine, token.tokenCol);
					this.ifelse_parser(ifstate, false);
					return ifstate;
				}else if(token.tokenType == TokenType.KW_FOR)
				{
					ForLoopStatement forstate = new ForLoopStatement(token.tokenLine, token.tokenCol);
					this.for_loop_parser(forstate);
					return forstate;
				}else if(token.tokenType == TokenType.KW_WHILE)
				{
					WhileStatement whilestate = new WhileStatement(token.tokenLine, token.tokenCol);
					this.while_loop_parser(whilestate);
					return whilestate;
				}else if(token.tokenType ==TokenType.KW_BREAK)
				{
					BreakStatement bk = new BreakStatement(token.tokenLine, token.tokenCol);
					return bk;
				}else if(token.tokenType == TokenType.KW_CONTINUE)
				{
					ContinueStatement ct = new ContinueStatement(token.tokenLine, token.tokenCol);
					return ct;
				}else if(token.tokenType == TokenType.KW_RETURN)
				{
					ReturnStatement ret = new ReturnStatement(token.tokenLine, token.tokenCol);
					Statement exp = this.statement_parser(false);
					ret.expression = exp;
					left = ret;
					// return也不会在左侧
					return left;
				}else if(token.tokenType == TokenType.KW_TRUE || token.tokenType == TokenType.KW_FALSE)
				{
					BooleanStatement b = new BooleanStatement(token.tokenStr, token.tokenLine, token.tokenCol);
					left = b;
					break;
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
				if(left.statementType != StatementType.IDENTIFIER)
				{
					this.PutToken();
					return left;
				}
				CallExpression ncall = new CallExpression(next_token.tokenLine, next_token.tokenCol);
				ncall.variable = left;
				do
				{
					Statement node = this.statement_parser(false);
					if(node != null)
					{
						ncall.params.add(node);
					}
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
				left = ncall;
				break;
			} // 字典取值 或者列表索引
			else if(next_token.tokenType == TokenType.LBRACK) //[
			{
				IndexExpression meb = new IndexExpression(next_token.tokenLine, next_token.tokenCol);
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
			else if(next_token.tokenType == TokenType.OP_ASSIGN)
			{
				AssignmentExpression assign = new AssignmentExpression(next_token.tokenLine, next_token.tokenCol);
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
				MemberExpression meb = new MemberExpression(next_token.tokenLine, next_token.tokenCol);
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
			BinaryExpression binExp = new BinaryExpression(next_token.tokenType, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol);
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
					left = UpdateBinaryExpressionPriority(binExp);
					break;
				default:
					this.PutToken();
					return left;
			}
		}
		return left;
	}
	/**
	 * 二元表达式的优先级的重新更新
	 * @param bin
	 * @return
	 */
	BinaryExpression UpdateBinaryExpressionPriority(BinaryExpression bin)
	{
		Statement left = bin.left;
		if(left.statementType == StatementType.EXPRESSION_BINARY && left.isWhole == false)
		{
			BinaryExpression l = (BinaryExpression)left;
			
			if(bin.opValue <  l.opValue)  // 优先级
			{
				BinaryExpression new_left = new BinaryExpression(l.opType, l.op, l.startLine, l.startColoumn);
				new_left.left = l.left;
				new_left.right = bin;
				bin.left = l.right;
				if(bin.right.statementType == StatementType.EXPRESSION_BINARY)
				{
					bin.right = UpdateBinaryExpressionPriority((BinaryExpression) bin.right);
				}
				return new_left;
			}
		}
		return bin;
	}
	Token ahead_token_ = null;
	Token curr_token = null;
	/**
	 * 获取一个token
	 * @return
	 */
	public Token GetToken()
	{
		if(ahead_token_ != null)
		{
			Token t = ahead_token_;
			ahead_token_ = null;
			curr_token =  t;
		}else
		{
			do
			{
				curr_token = tokenReader_.GetToken();
			}while(curr_token != null && curr_token.tokenType == TokenType.COMMENT);
		}
		return curr_token;
	}
	/**
	 * 放回一个token
	 */
	public void PutToken()
	{
		ahead_token_ = curr_token;
		curr_token = null;
	}
}
