package org.jeff.lune.parsers.objs;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.token.Token;

/**
 * 字符串节点
 * @author JeffXun
 *
 */
public class StringStatement extends Statement
{
	public String value;
	public StringStatement(Token token)
	{
			this.value = token.tokenStr;
			this.startLine = token.tokenLine;
			this.startColoumn = token.tokenCol;
			this.statementType = StatementType.STRING;
	}
	
	@Override
	public String toString() 
	{
		return this.value;
	}

	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		return new LuneObject(value);
	}
}
