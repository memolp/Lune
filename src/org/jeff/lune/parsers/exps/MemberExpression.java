package org.jeff.lune.parsers.exps;

import org.jeff.lune.parsers.ExpressionStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;

public class MemberExpression extends ExpressionStatement
{
	public Statement parent;
	public Statement child;
	public MemberExpression()
	{
			this.statementType = StatementType.MEMBER;
	}
}
