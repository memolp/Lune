package org.jeff.lune.parsers;

import java.util.List;

import org.jeff.lune.token.Token;
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
	public void parser()
	{
		this.block_parser();
	}
	
	Statement statement_parser(Token token)
	{
		if(token.tokenType == TokenType.KW_FUNCTION)
		{
			return this.function_parser(token);
		}else if(token.tokenType == TokenType.KW_FOR)
		{
			return this.for_loop_parser(token);
		}else if(token.tokenType == TokenType.KW_WHILE)
		{
			return this.while_loop_parser(token);
		}else if(token.tokenType == TokenType.KW_IF)
		{
			return this.ifelse_parser(token);
		}else
		{
			return this.expression_parser(token);
		}
	}
	
	static final String SYNTAX_ERROR = "syntax error , unexpceted `%s` in file:%s, line:%s, column:%s";
	BlockStatement block_parser()
	{
		BlockStatement block = new BlockStatement();
		Token token = this.GetToken();
		if(token == null || token.tokenType == TokenType.RCURLY )
		{}
		this.statement_parser(token);
		return block;
	}
	FunctionStatement function_parser(Token token)
	{
		FunctionStatement function = new FunctionStatement();
		// 函数仅支持 xx.x.xx = function()
		Token next_token = this.GetToken();
		if(next_token.tokenType != TokenType.LPAREN)
		{
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		}
		this.function_params_parser(this.GetToken());
		next_token = this.GetToken();
		if(next_token.tokenType != TokenType.LCURLY)
		{
			throw new RuntimeException(String.format(SYNTAX_ERROR, next_token.tokenStr, next_token.tokenLine, next_token.tokenCol));
		}
		this.block_parser();
		return function;
	}
	
	ParamsStatement function_params_parser(Token token)
	{
		ParamsStatement params = new ParamsStatement();
		do
		{
			if(token.tokenType == TokenType.RPAREN)
				break;
			if(token.tokenType != TokenType.IDENTIFIER)
			{
				throw new RuntimeException(String.format(SYNTAX_ERROR, token.tokenStr, token.tokenLine, token.tokenCol));
			}
			params.PushParam();
			token = this.GetToken();
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
	
	ExpressionStatement expression_parser(Token token)
	{
		ExpressionStatement exp_state = new ExpressionStatement();
		return exp_state;
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
