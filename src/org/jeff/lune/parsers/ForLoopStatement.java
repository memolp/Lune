package org.jeff.lune.parsers;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.parsers.exps.ParamsExpression;
import org.jeff.lune.parsers.objs.IdentifierStatement;

public class ForLoopStatement extends Statement
{
	public ParamsExpression params = null;
	public Statement iterObject = null;
	public BlockStatement body = null;
	public ForLoopStatement()
	{
		this.statementType = StatementType.FOR;
	}
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		LuneObject obj = iterObject.OnExecute(rt, null);
		if(obj.objType == LuneObjectType.LIST)
		{
			LuneListObject list = (LuneListObject)obj;
			if(params.params.size() != 1) throw new RuntimeException();
			Statement p = params.params.get(0);
			if(p.statementType != StatementType.IDENTIFIER) throw new RuntimeException();
			IdentifierStatement pIdt = (IdentifierStatement)p;
			list.beginIter();
			while(list.hasNext())
			{
				rt.CurrentNamespace().AddSymbol(pIdt.name, list.nextElement());
				this.body.OnExecute(rt, null);
				if(rt.IsBreakFlag || rt.IsReturnFlag) break;
				rt.IsContinueFlag = false;
			}
		}
		rt.IsBreakFlag = false;
		rt.IsContinueFlag = false;
		return null;
	}

}
