package org.jeff.lune.parsers;

import java.io.Reader;
import java.util.List;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.exps.AssignmentExpression;
import org.jeff.lune.parsers.exps.BinaryExpression;
import org.jeff.lune.parsers.exps.BlockStatement;
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
import org.jeff.lune.parsers.objs.NoneStatement;
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
 *  TODO 已移除缓存变量、数字、字符串等语句的部分。
 */
public class SyntaxParser
{
	private static final String UNEXPCETED_SYMBOL = "syntax error , unexpceted symbol `%s` at %s:%s";
	private static final String EXPCETED_SYMBOL = "syntax error, `%s` expceted at %s:%s";
	private static final String CODE_ERROR = "syntax error, code error at %s:%s";
	
	/** Token读取器 */
	private TokenReader tokenReader_;
	/** 记录上一个Token */
	private Token ahead_token_ = null;
	/** 当前的Token */
	private Token curr_token = null;
	/** 文件名称 */
	private String file_;
	/** 当前文件的语句体 */
	private ProgramStatement program_;
	/** Lune 运行时 */
	private LuneRuntime luneRT_;
	/**
	 * 创建语法解析器
	 * @param rt  运行时
	 * @param reader  支持mark 和 reset
	 * @param filename  文件名
	 */
	public SyntaxParser(LuneRuntime rt, Reader reader, String filename)
	{
		this.luneRT_ = rt;
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
		// 解析完成后就清理数据
		this.curr_token = null;
		this.ahead_token_= null;
		this.tokenReader_ = null;
	}
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
			state.parent = block;  // 建立层级树结构
			block.AddStatement(state);
		}while(true);
	}
	/**
	 * 函数声明解析（解析参数和函数体内部）
	 * @param function
	 */
	void function_parser(FunctionExpression function)
	{
		do
		{
			// 函数仅支持 xx.x.xx = function()
			Token next_token = this.GetToken();
			// 判空
			if(next_token == null)
			{
				this.luneRT_.SyntaxError(EXPCETED_SYMBOL,  "(", this.file_, function.startLine);
				break;
			}
			// 函数后面紧跟括号 暂时不支持函数名放function后面。
			if(next_token.tokenType != TokenType.LPAREN)
			{
				this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL,  next_token.tokenStr, this.file_, next_token.tokenLine);
				return;
			}
			this.function_params_parser(function);
			next_token = this.GetToken();
			// 判空
			if(next_token == null)
			{
				this.luneRT_.SyntaxError(EXPCETED_SYMBOL,  "{", this.file_, function.startLine);
				return;
			}
			// 函数体包含在{}之间
			if(next_token.tokenType != TokenType.LCURLY)
			{
				this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL,  next_token.tokenStr, this.file_, next_token.tokenLine);
				return;
			}
			BlockStatement body = new BlockStatement(next_token.tokenLine, next_token.tokenCol);
			this.block_parser(body);
			body.parent = function; // ----语句树
			function.body = body;
			next_token = this.GetToken();
			if(next_token == null)
			{
				this.luneRT_.SyntaxError(EXPCETED_SYMBOL,  "}", this.file_, function.startLine);
				return;
			}
			if(next_token.tokenType != TokenType.RCURLY)
			{
				this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL,  next_token.tokenStr, this.file_, next_token.tokenLine);
				return;
			}
		}while(false);
	}
	/**
	 * 函数的参数，只能是简单的变量
	 * @return
	 */
	void function_params_parser(FunctionExpression func)
	{
		List<Statement> params = func.params;
		do
		{
			Token token = this.GetToken();
			// 防空
			if(token == null)
			{
				this.luneRT_.SyntaxError(CODE_ERROR, this.file_,  func.startLine);
				return;
			}
			// 找到右括号则结束
			if(token.tokenType == TokenType.RPAREN )
				break;
			// 函数的参数只能是变量标识符
			if(token.tokenType == TokenType.IDENTIFIER)
			{
				// TODO 这里每次都会创建新的标识符，移除缓存是方便做堆栈。具体后面再看。
				IdentifierStatement idt =  new IdentifierStatement(token.tokenStr, token.tokenLine, token.tokenCol);
				idt.parent = func; // ----语句树
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
				this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL,  token.tokenStr, this.file_, token.tokenLine);
				return;
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
		// 防空
		if(token == null)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "(", this.file_, for_state.startLine);
			return;
		}
		// for 后面紧接(
		if(token.tokenType  != TokenType.LPAREN)
		{
			this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL, token.tokenStr, this.file_, token.tokenLine);
			return;
		}
		do
		{
			Token next_token = this.GetToken();
			if(next_token == null)
			{
				this.luneRT_.SyntaxError(CODE_ERROR, this.file_,  for_state.startLine);
				return;
			}
			if(next_token.tokenType == TokenType.COMMA)
				continue;
			if(next_token.tokenType ==TokenType.IDENTIFIER)
			{
				IdentifierStatement idt = new IdentifierStatement(next_token.tokenStr, next_token.tokenLine, next_token.tokenCol);
				idt.parent = for_state;  // ----语句树
				for_state.params.add(idt);
			}
			else if(next_token.tokenType == TokenType.KW_IN)
			{
				break;
			}
			else
			{
				this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL, next_token.tokenStr, this.file_, next_token.tokenLine);
				return;
			}
		}while(true);
		// 被迭代的对象
		Statement iter = this.statement_parser(false);
		if(iter == null)
		{
			this.luneRT_.SyntaxError(CODE_ERROR, this.file_,  for_state.startLine);
			return;
		}
		iter.parent = for_state;  // ----语句树
		for_state.iterObject = iter;
		Token next_token = this.GetToken();
		// 必须是）
		if(next_token == null || next_token.tokenType != TokenType.RPAREN)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, ")", this.file_,  for_state.startLine);
			return;
		}
		next_token = this.GetToken();
		// 必须是{
		if(next_token == null || next_token.tokenType != TokenType.LCURLY)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "{", this.file_,  for_state.startLine);
			return;
		}
		BlockStatement body = new BlockStatement(next_token.tokenLine, next_token.tokenCol);
		this.block_parser(body);
		body.parent = for_state; // ----语句树
		for_state.body = body;
		next_token = this.GetToken();
		if(next_token == null || next_token.tokenType != TokenType.RCURLY)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "}", this.file_,  for_state.startLine);
			return;
		}
	}
	/**
	 * while解析
	 * @param wstate
	 */
	void  while_loop_parser(WhileStatement wstate)
	{
		Token token = this.GetToken();
		if(token == null || token.tokenType  != TokenType.LPAREN)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "(", this.file_,  wstate.startLine);
			return;
		}
		Statement exp = this.statement_parser(false);
		exp.parent = wstate; // ----语句树
		wstate.condition = exp;
		Token next_token = this.GetToken();
		if(token == null || next_token.tokenType != TokenType.RPAREN)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, ")", this.file_,  wstate.startLine);
			return;
		}
		next_token = this.GetToken();
		if(token == null || next_token.tokenType != TokenType.LCURLY)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "{", this.file_,  wstate.startLine);
			return;
		}
		BlockStatement body =  new BlockStatement(next_token.tokenLine, next_token.tokenCol);
		this.block_parser(body);
		body.parent = wstate; // ----语句树
		wstate.body = body;
		next_token = this.GetToken();
		if(next_token == null || next_token.tokenType != TokenType.RCURLY)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "{", this.file_,  wstate.startLine);
			return;
		}
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
			if(token == null || token.tokenType  != TokenType.LPAREN)
			{
				this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "(", this.file_,  if_state.startLine);
				return;
			}
			Statement exp = this.statement_parser(false);
			exp.parent = if_state; // ----语句树
			if_state.condition = exp;
			Token next_token = this.GetToken();
			if(next_token == null || next_token.tokenType != TokenType.RPAREN)
			{
				this.luneRT_.SyntaxError(EXPCETED_SYMBOL, ")", this.file_,  if_state.startLine);
				return;
			}
		}
		Token next_token = this.GetToken();
		if(next_token == null || next_token.tokenType != TokenType.LCURLY)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "{", this.file_,  if_state.startLine);
			return;
		}
		BlockStatement body =  new BlockStatement(next_token.tokenLine, next_token.tokenCol);
		this.block_parser(body);
		body.parent = if_state; // ----语句树
		if_state.body = body;
		next_token = this.GetToken();
		if(next_token == null || next_token.tokenType != TokenType.RCURLY)
		{
			this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "}", this.file_,  if_state.startLine);
			return;
		}
		next_token = this.GetToken();
		if(next_token == null) // 后面已经没有其他代码了。
		{
			return;
		}
		// 如果有elif分支
		if(next_token.tokenType ==TokenType.KW_ELIF)
		{
			IfElseStatement branch = new IfElseStatement(StatementType.ELIF, next_token.tokenLine, next_token.tokenCol);
			this.ifelse_parser(branch, false);
			branch.parent = if_state.parent; // ----语句树 特殊处理
			if_state.Switch = branch;
		}
		else if(next_token.tokenType == TokenType.KW_ELSE)
		{
			IfElseStatement branch = new IfElseStatement(StatementType.ELSE, next_token.tokenLine, next_token.tokenCol);
			this.ifelse_parser(branch, true);
			branch.parent = if_state.parent;  // ----语句树 特殊处理
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
							this.luneRT_.SyntaxError(CODE_ERROR, this.file_,  next_token.tokenLine);
							return null;
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
					left = new NumberStatement(token.tokenStr, token.isDoubleValue, token.tokenLine, token.tokenCol);
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
							element.parent = nlist;  // ----语句树
							nlist.AddElement(element);
						}
						
						// 看下一个元素是,还是]
						Token next_token = this.GetToken();
						if(next_token == null)
						{
							this.luneRT_.SyntaxError(CODE_ERROR,  this.file_, token.tokenLine);
						}
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
									this.luneRT_.SyntaxError(CODE_ERROR,  this.file_, token.tokenLine);
									return null;
								}
								if(next_token.tokenType != TokenType.COLON)
								{
									this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL, next_token.tokenStr, this.file_, next_token.tokenLine);
									return null;
								}
								// 获取值
								Statement value = this.statement_parser(false);
								if(value == null) //不可没有值
								{
									this.luneRT_.SyntaxError(CODE_ERROR,  this.file_, next_token.tokenLine);
									return null;
								}
								key.parent = ndict;
								value.parent = ndict;
								ndict.Add(key, value);
							}
							// 继续解析下一个
							Token next_token = this.GetToken();
							if(next_token == null )
							{
								this.luneRT_.SyntaxError(CODE_ERROR,  this.file_, token.tokenLine);
								return null;
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
						this.luneRT_.SyntaxError(CODE_ERROR,  this.file_, token.tokenLine);
						return null;
					}
					else if(next_token.tokenType != TokenType.RPAREN)
					{
						this.luneRT_.SyntaxError(EXPCETED_SYMBOL, ")", this.file_, next_token.tokenLine);
						return null;
					}
					left.isWhole = true;
				}
				// 取not
				else if(token.tokenType == TokenType.OP_NOT)
				{
					Statement value = this.statement_parser(true);
					if(value == null)
					{
						this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL, token.tokenStr, this.file_, token.tokenLine);
					}
					left = new UnaryExpression(TokenType.OP_NOT, value, token.tokenLine, token.tokenCol);
					value.parent = left;  // ----语句树
				}// 取反
				else if(token.tokenType == TokenType.OP_BIT_XOR)
				{
					Statement value = this.statement_parser(true);
					if(value == null)
					{
						this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL, token.tokenStr,  this.file_, token.tokenLine);
					}
					left = new UnaryExpression(TokenType.OP_NOT, value, token.tokenLine, token.tokenCol);
					value.parent = left;  // ----语句树
				}// 取负数
				else if(token.tokenType == TokenType.OP_MINUS)
				{
					Token next_token = this.GetToken();
					if(next_token == null || next_token.tokenType != TokenType.NUMBER)
					{
						this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL, token.tokenStr,  this.file_, token.tokenLine);
					}
					NumberStatement num =  new NumberStatement(token.tokenStr, token.isDoubleValue, token.tokenLine, token.tokenCol);
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
					exp.parent = ret;  // ----语句树
					left = ret;
					// return也不会在左侧
					return left;
				}else if(token.tokenType == TokenType.KW_TRUE || token.tokenType == TokenType.KW_FALSE)
				{
					BooleanStatement b = new BooleanStatement(token.tokenStr, token.tokenLine, token.tokenCol);
					left = b;
					break;
				}else if(token.tokenType == TokenType.KW_NONE)
				{
					NoneStatement none = new NoneStatement(token.tokenLine,  token.tokenCol);
					left = none;
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
							this.luneRT_.SyntaxError(UNEXPCETED_SYMBOL, token.tokenStr, this.file_, token.tokenLine);
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
				ncall.parent = left;  // ----语句树
				do
				{
					Statement node = this.statement_parser(false);
					if(node != null)
					{
						node.parent = ncall;  // ----语句树
						ncall.params.add(node);
					}
					Token next_tk = this.GetToken();
					if(next_tk == null)
					{
						this.luneRT_.SyntaxError(CODE_ERROR, this.file_, next_token.tokenLine);
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
				meb.parent = left;  // ----语句树
				
				Statement child = this.statement_parser(false);
				if(child == null)
				{
					this.luneRT_.SyntaxError(CODE_ERROR, this.file_, next_token.tokenLine);
				}
				Token next = this.GetToken();
				if(next == null)
				{
					this.luneRT_.SyntaxError(CODE_ERROR, this.file_, next_token.tokenLine);
				}
				if(next.tokenType != TokenType.RBRACK)
				{
					this.luneRT_.SyntaxError(EXPCETED_SYMBOL, "]", this.file_, next_token.tokenLine);
				}
				meb.index = child;
				child.parent = meb;  // ----语句树
				left = meb;
				continue;
			}// 赋值语句
			else if(next_token.tokenType == TokenType.OP_ASSIGN)
			{
				AssignmentExpression assign = new AssignmentExpression(next_token.tokenLine, next_token.tokenCol);
				assign.variable =left;
				assign.parent = left;  // ----语句树
				
				Statement value = this.statement_parser(false);
				if(value == null)
				{
					this.luneRT_.SyntaxError(CODE_ERROR, this.file_, next_token.tokenLine);
				}
				assign.value = value;
				value.parent = assign;  // ----语句树
				left = assign;
				break;
			}// 属性索引 dot
			else if(next_token.tokenType == TokenType.OP_DOT)
			{
				MemberExpression meb = new MemberExpression(next_token.tokenLine, next_token.tokenCol);
				meb.parent = left;  // ----语句树
				Statement child = this.statement_parser(true);
				if(child == null)
				{
					this.luneRT_.SyntaxError(CODE_ERROR, this.file_, next_token.tokenLine);
				}
				meb.child = child;
				child.parent = meb; // ----语句树
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
					{
						this.luneRT_.SyntaxError(CODE_ERROR, this.file_, next_token.tokenLine);
					}
					left.parent = binExp; // ----语句树
					right.parent = binExp; // ----语句树
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
	/**
	 * 获取一个token
	 * @return
	 */
	private Token GetToken()
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
				// 过滤注释
			}while(curr_token != null && curr_token.tokenType == TokenType.COMMENT);
		}
		return curr_token;
	}
	/**
	 * 放回一个token
	 */
	private void PutToken()
	{
		ahead_token_ = curr_token;
		curr_token = null;
	}
	/**
	 * 运行代码并返回结果
	 * @return
	 */
	public LuneObject Execute()
	{
		return this.program_.OnExecute(this.luneRT_, null);
	}
}
