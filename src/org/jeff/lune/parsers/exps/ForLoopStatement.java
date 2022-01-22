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
		// 执行迭代逻辑 - 实际上只有列表和字典的迭代
		LuneObject obj = iterObject.OnExecute(rt, null);
		// 列表的迭代逻辑
		if(obj.objType == LuneObjectType.LIST)
		{
			LuneListObject list = (LuneListObject)obj;
			// 列表的迭代，for k in b ==> 其中k只能允许1个。
			if(params.size() != 1)
			{
				rt.RuntimeError(this, "for 列表迭代参数仅支持1个，而提供了多个:%s", this.params);
			}
			// 参数只能是变量标识符
			Statement p = params.get(0);
			if(p.statementType != StatementType.IDENTIFIER)
			{
				rt.RuntimeError(this, "for 迭代参数仅支持普通的变量, 而提供的类型:%s", p.statementType);
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
			int params_count = params.size();
			// 如果参数是1个，就只迭代key， 如果是2个，就是key和value一起
			if(params_count > 2 || params_count < 1)
			{
				rt.RuntimeError(this, "for 字典迭代支持1~2个参数，而实际提供了%s个", params_count);
			}
			Statement key = params.get(0);
			Statement value  = null;
			if(params_count == 2)
			{
				value = params.get(1);
			}
			if(key.statementType != StatementType.IDENTIFIER)
			{
				rt.RuntimeError(this, "for 迭代参数仅支持普通的变量, 而提供的名称:%s 类型:%s", key, key.statementType);
			}
			if(value != null && value.statementType != StatementType.IDENTIFIER)
			{
				rt.RuntimeError(this, "for 迭代参数仅支持普通的变量, 而提供的名称:%s 类型:%s", value, value.statementType);
			}
			IdentifierStatement keyId = (IdentifierStatement)key;
			IdentifierStatement valueId = null;
			if(params_count == 2)
			{
				valueId = (IdentifierStatement)value;
			}
			// map的迭代对象
			Iterator<Entry<LuneObject, LuneObject>> iterator = map.iterator();
			Entry<LuneObject, LuneObject> entry;
			while(iterator.hasNext())
			{
				entry = iterator.next();
				rt.CurrentNamespace().AddSymbol(keyId.name, entry.getKey());
				if(params_count == 2)
					rt.CurrentNamespace().AddSymbol(valueId.name, entry.getValue());
				this.body.OnExecute(rt, null);
				if(rt.IsBreakFlag || rt.IsReturnFlag) break;
				rt.IsContinueFlag = false;
			}
		}
		// 不支持的迭代
		else
		{
			rt.RuntimeError(this, "for 无法迭代该类型:%s", obj.objType);
		}
		// 迭代结束后需要重置break和continue标记
		rt.IsBreakFlag = false;
		rt.IsContinueFlag = false;
		return LuneObject.noneLuneObject;
	}

}
