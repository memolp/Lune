package org.jeff.lune.object;

import java.util.List;

import org.jeff.lune.LuneNamespace;
import org.jeff.lune.LuneNamespaceType;
import org.jeff.lune.LuneRuntime;
import org.jeff.lune.parsers.exps.Statement;

public class LuneClassObject extends LunePropertyObject
{
	private LuneClassObject base_;
	public LuneClassObject(String className)
	{
		this.objType = LuneObjectType.CLASS;
		this.base_ = null;
		this.value_ = className;
	}
	
	public LuneClassObject(String className, LuneClassObject parent)
	{
		this.objType = LuneObjectType.CLASS;
		this.value_ = className;
		this.base_ = parent;
	}
	
	LuneClassObject(LuneClassObject cls)
	{
		this.objType = LuneObjectType.CLASS;
		this.value_ = cls.strValue();
		this.base_ = cls;
	}
	/**
	 * 创建类的实例
	 * @param rt
	 * @param params
	 * @return 返回实例对象
	 * @throws Exception
	 */
	public LuneObject Exceute(LuneRuntime rt,  List<Statement> params) throws Exception
	{
		LuneClassInstance obj = new LuneClassInstance(this);  // 把Class对象传进去，方便实例执行class的方法。
		// 创建好实例后，就需要调用构造函数。
		LuneObject ctor_ = obj.GetAttribute("ctor");  // ctor未固定的名称
		if(ctor_.objType != LuneObjectType.FUNCTION) 
		{
			throw new Exception(String.format("类:%s 的ctor构造函数名称被覆盖，无法创建对象", this.strValue()));
		}
		LuneFunction ctor_func = (LuneFunction)ctor_;
		// 创建临时命名空间
		LuneNamespace temp_func_namespace = new LuneNamespace(LuneNamespaceType.FUNCTION, rt.CurrentNamespace());
		// 将命名空间放入栈顶
		rt.PushNamespace(temp_func_namespace);
		temp_func_namespace.AddSymbol("self", obj);  // 将实例对象给self, 并放入命名空间。
		ctor_func.Exceute(rt,  params, true);  // 形参转实参到LuneFunction内部处理。
		// 函数执行完成后移除命名空间
		rt.PopNamespace();
		return obj;  // 注意虽然调用了构造函数，但是最后返回的应该还是实例对象。
	}
	
	@Override
	public LuneObject GetAttribute(String name)
	{
		if(this.__attributes.containsKey(name))
		{
			return this.__attributes.get(name);
		}
		else if(this.base_ != null)
		{
			return this.base_.GetAttribute(name);
		}
		return LuneObject.noneLuneObject;
	}
}
