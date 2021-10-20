package org.jeff.lune.parsers.exps;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneListObject;
import org.jeff.lune.object.LuneMapObject;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.parsers.objs.IdentifierStatement;

/**
 * for迭代器
 * for(k in nlist) { ... .. }  列表的元素遍历
 * for(k,v in ndict) { ... .. } 字典的元素遍历
 * for(i in range(n)) { ... .. } 伪循环，其实是通过range生成了有序的列表
 * @author 覃贵锋
 *
 */
public class ForLoopStatement extends Statement
{
	/** for的参数  */
	public List<Statement> params = null;
	/** 被迭代的变量  */
	public Statement iterObject = null;
	/** 内部的语句块 */
	public BlockStatement body = null;
	/**
	 * for迭代器
	 * @param line
	 * @param col
	 */
	public ForLoopStatement(int line, int col)
	{
		super(StatementType.FOR, line, col);
		this.params = new LinkedList<Statement>();
	}
	
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		rt.PushBlockType(BlockStatementType.LOOP_BLOCK);
		// 执行迭代逻辑 - 实际上只有列表和字典的迭代
		LuneObject obj = iterObject.OnExecute(rt, null);
		// 列表的迭代逻辑
		if(obj.objType == LuneObjectType.LIST)
		{
			LuneListObject list = (LuneListObject)obj;
			// 列表的迭代，for k in b ==> 其中k只能允许1个。
			if(params.size() != 1)
			{
				throw new RuntimeException();
			}
			// 参数只能是变量标识符
			Statement p = params.get(0);
			if(p.statementType != StatementType.IDENTIFIER)
			{
				throw new RuntimeException();
			}
			IdentifierStatement pIdt = (IdentifierStatement)p;
			// 获取list的迭代器对象
			Iterator<LuneObject> iterator = list.iterator();
			while(iterator.hasNext())
			{
				// 将参数的值更新后，开始执行内部body
				rt.CurrentNamespace().AddSymbol(pIdt.name, iterator.next());
				this.body.OnExecute(rt, null);
				// 如果遇到break和return 就结束循环
				if(rt.IsBreakFlag || rt.IsReturnFlag) break;
				// 每次执行完body都需要进行continue的标记重置。
				rt.IsContinueFlag = false;
			}
		}
		// 字典的遍历逻辑，与list类似
		else if(obj.objType == LuneObjectType.MAP)
		{
			LuneMapObject map = (LuneMapObject)obj;
			if(params.size() != 2)
			{
				throw new RuntimeException();
			}
			Statement key = params.get(0);
			Statement value = params.get(1);
			if(key.statementType != StatementType.IDENTIFIER || value.statementType != StatementType.IDENTIFIER)
			{
				throw new RuntimeException();
			}
			IdentifierStatement keyId = (IdentifierStatement)key;
			IdentifierStatement valueId = (IdentifierStatement)value;
			// map的迭代对象
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
		}
		// 不支持的迭代
		else
		{
			throw new RuntimeException();
		}
		// 迭代结束后需要重置break和continue标记
		rt.PopBlockType();
		rt.IsBreakFlag = false;
		rt.IsContinueFlag = false;
		return null;
	}

}
