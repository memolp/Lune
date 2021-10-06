package org.jeff.lune.parsers.objs;

import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.token.Token;

public class NumberStatement extends Statement
{
	public double value;
	public NumberStatement(Token token)
	{
		this.value = Double.parseDouble(token.tokenStr);
		this.startLine = token.tokenLine;
		this.startColoumn = token.tokenCol;
		this.statementType = StatementType.NUMBER;
	}
	
	@Override
	public String toString() 
	{
		return String.valueOf(this.value);
	}
}
