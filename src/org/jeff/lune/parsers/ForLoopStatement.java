package org.jeff.lune.parsers;

import java.util.Iterator;
import java.util.Map.Entry;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneMapObject;
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
			Iterator<LuneObject> iterator = list.iterator();
			while(iterator.hasNext())
			{
				rt.CurrentNamespace().AddSymbol(pIdt.name, iterator.next());
				this.body.OnExecute(rt, null);
				if(rt.IsBreakFlag || rt.IsReturnFlag) break;
				rt.IsContinueFlag = false;
			}
		}else if(obj.objType == LuneObjectType.MAP)
		{
			LuneMapObject map = (LuneMapObject)obj;
			if(params.params.size() != 2) throw new RuntimeException();
			Statement key = params.params.get(0);
			Statement value = params.params.get(1);
			if(key.statementType != StatementType.IDENTIFIER || value.statementType != StatementType.IDENTIFIER)
				throw new RuntimeException();
			IdentifierStatement keyId = (IdentifierStatement)key;
			IdentifierStatement valueId = (IdentifierStatement)value;
			Iterator<Entry<LuneObject, LuneObject>> iterator = map.iterator();
			Entry<LuneObject, LuneObject> entry;
			while(iterator.hasNext())
			{
				entry = iterator.next();
				rt.CurrentNamespace().AddSymbol(keyId.name, entry.getKey());
				rt.CurrentNamespace().AddSymbol(valueId.name, entry.getValue());
				this.body.OnExecute(rt, null);
				if(rt.IsBreakFlag || rt.IsReturnFlag) break;
				rt.IsContinueFlag = false;
			}
		}else
		{
			throw new RuntimeException();
		}
		rt.IsBreakFlag = false;
		rt.IsContinueFlag = false;
		return null;
	}

}
