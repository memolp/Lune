package org.jeff.lune.parsers.exps;

import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.token.TokenType;

public class UnaryExpression extends ExpressionStatement 
{
	public TokenType  opType;
	public Statement variable;
	public UnaryExpression(TokenType type, Statement node)
	{
		this.opType = type;
		this.variable = node;
		this.statementType = StatementType.EXPRESSION_UNARY;
	}
}
