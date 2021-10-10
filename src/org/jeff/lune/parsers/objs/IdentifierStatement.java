package org.jeff.lune.parsers.objs;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.token.Token;

/**
 * 变量标识符
 * @author JeffXun
 *
 */
public class IdentifierStatement extends Statement
{
	/** 变量名称 */
	public String name;
	public IdentifierStatement(Token token)
	{
		this.name = token.tokenStr;
		this.startLine = token.tokenLine;
		this.startColoumn = token.tokenCol;
		this.statementType = StatementType.IDENTIFIER;
	}
	
	@Override
	public String toString() 
	{
		return this.name;
	}

	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		if(object == null)
		{
			return rt.CurrentNamespace().GetSymbol(this.name);
		}else
		{
			return object.GetAttribute(this.name);
		}
	}
}
