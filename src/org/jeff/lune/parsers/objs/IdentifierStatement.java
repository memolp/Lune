package org.jeff.lune.parsers.objs;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;

/**
 * 变量标识符
 * 1. 全局变量
 * 2. 局部变量
 * 3. 函数变量
 * 4. 参数变量
 * 5. 属性变量
 * @author 覃贵锋
 *
 */
public class IdentifierStatement extends Statement
{
	/** 变量名称 */
	public String name;
	/**
	 * 创建变量标识符
	 * @param val
	 * @param line
	 * @param col
	 */
	public IdentifierStatement(String val, int line, int col)
	{
		super(StatementType.IDENTIFIER, line, col);
		this.name = val;
	}
	/** 缓存变量的值 */  //TODO 需要待优化，需要考虑更新以及如何指定更新了。
	public LuneObject cache_value = null;
	/**
	 * 变量参数执行，相当于获取变量的值。
	 * 解释：变量本身只是一个字符串，它的值存储在命名空间里面或者其父对象的属性上面。
	 */
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		//TODO a = a + a 这种会执行3次查找，因此需要优化。（查完缓存，只有在赋值后修改）
		// 变量标识符主要用于查找对象
		if(object == null)
		{
			// 没有object时，去命名空间查 -- 先不缓存，这个逻辑目前写有问题
			//if(cache_value == null) // 并且先判断缓存
			cache_value =  rt.CurrentNamespace().GetSymbol(this.name);
			return cache_value;
		}else
		{
			// 有object则通过属性查
			return object.GetAttribute(this.name);
		}
	}
	
	@Override
	public String toString() 
	{
		return this.name;
	}
	
}
