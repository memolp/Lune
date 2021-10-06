package org.jeff.lune.parsers.exps;

import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.token.TokenType;

public class BinaryExpression extends ExpressionStatement 
{
	public TokenType opType;
	public String op;
	public int opValue;
	public Statement left;
	public Statement right;
	public BinaryExpression(TokenType type, String opn)
	{
		this.opType = type;
		this.op = opn;
		this.statementType = StatementType.EXPRESSION_BINARY;
		switch(type)
		{
			case OP_BIT_NOT:
			case OP_NOT:
				this.opValue = 2;
				break;
			case OP_PLUS:
			case OP_MINUS:
				this.opValue = 4;
				break;
			case OP_MULTI:
			case OP_DIV:
			case OP_MOD:
				this.opValue = 3;
				break;
			case OP_BIT_LEFT:
			case OP_BIT_RIGHT:
				this.opValue = 5;
				break;
			case OP_LE:
			case OP_LT:
			case OP_GE:
			case OP_GT:
				this.opValue = 6;
				break;
			case OP_EQ:
			case OP_NE:
				this.opValue = 7;
			case OP_BIT_AND:
				this.opValue = 8;
				break;
			case OP_BIT_XOR:
				this.opValue = 9;
				break;
			case OP_BIT_OR:
				this.opValue = 10;
				break;
			case OP_AND:
				this.opValue = 11;
				break;
			case OP_OR:
				this.opValue = 12;
				break;
			default:
				this.opValue = 0;
		}
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.left.toString());
		sb.append(this.op);
		sb.append(this.right.toString());
		sb.append(")");
		return sb.toString();
	}
}


