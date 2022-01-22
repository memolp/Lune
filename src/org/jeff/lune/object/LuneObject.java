package org.jeff.lune.object;

import java.util.HashMap;
import java.util.Map;

import org.jeff.lune.exceptions.NotImplementedException;

/**
 * 全部Lune对象的基类 也是真的全部皆对象。但是性能上还是有很大的损失。
 * @author 覃贵锋
 *
 */
public class LuneObject extends Object
{
	/** 常量 true */
	public static LuneObject trueLuneObject = new LuneObject(true);
	/** 常量 false */
	public static LuneObject falseLuneObject = new LuneObject(false);
	/** 常量 None */
	public static LuneObject noneLuneObject = new LuneObject();
	/** 数字常量池 */
	//public static Map<Double, LuneObject> constDoubleObject = new  HashMap<Double, LuneObject>();
	public static Map<Long, LuneObject> constLongObject = new HashMap<Long, LuneObject>();
	/** 一开始创建-2. 299的常量数字 */
	static 
	{
		for(long i = -2; i < 300; i+=1)
		{
			//constDoubleObject.put((double) i, new LuneObject(i));
			constLongObject.put(i, new LuneObject(i));
		}
	}
	/**
	 * 创建数字变量-会缓存部分
	 * @param v
	 * @return
	 */
	public static LuneObject CreateLongObject(long v)
	{
		LuneObject res = constLongObject.get(v);
		if(res == null)
		{
			res = new LuneObject(v);
		}
		return res;
	}
	/**
	 *  创建数字变量-会缓存部分
	 * @param v
	 * @return
	 */
	public static LuneObject CreateDoubleObject(double v)
	{
		/*LuneObject res = constDoubleObject.get(v);
		if(res == null)
		{
			res = new LuneObject(v);
		}
		return res;*/
		return new LuneObject(v);
	}
	/**
	 * 创建boolean变量- 直接使用缓存
	 * @param v
	 * @return
	 */
	public static LuneObject CreateBooleanObject(boolean v)
	{
		if(v) return trueLuneObject;
		return falseLuneObject;
	}
	/** 对象的类型 */
	public LuneObjectType objType = LuneObjectType.None;
	/** 实际的对象 - LuneObject实际是对象value的外部包装 */
	protected Object value_ = null;
	/** 创建默认的LuneObject对象，value_为空值 */
	public LuneObject()
	{
		this.objType = LuneObjectType.None;
	}
	/**
	 * 对象是整数
	 * @param v
	 */
	public LuneObject(long v)
	{
		this.value_ = v;
		this.objType = LuneObjectType.NUMBER;
	}
	/**
	 * 对象是数值
	 * @param v
	 */
	public LuneObject(double v)
	{
		this.value_ = v;
		this.objType = LuneObjectType.NUMBER;
	}
	/**
	 * 对象是布尔值
	 * @param v
	 */
	public LuneObject(boolean v)
	{
		this.value_ = v;
		this.objType = LuneObjectType.BOOL;
	}
	/**
	 * 对象是字符串
	 * @param v
	 */
	public LuneObject(String v)
	{
		this.value_ = v;
		if(v != null)
		{
			this.objType = LuneObjectType.STRING;
		}	
	}
	/**
	 * 对象是Java的Object对象
	 * @param v
	 */
	public LuneObject(Object v)
	{
		this.value_ = v;
		if(v != null)
		{
			this.objType = LuneObjectType.OBJECT;
		}
	}
	/**
	 * 返回值
	 * @return
	 */
	public Object GetValue()
	{
		return  this.value_;
	}
	/**
	 * 转换成数字
	 * @return
	 * @throws Exception 
	 */
	public double doubleValue() throws Exception
	{
		// bool 值返回0和1
		if(this.objType == LuneObjectType.BOOL)
		{
			if((boolean)this.value_) return 1;
			return 0;
		}
		try
		{
			return Double.parseDouble(this.strValue());
		}catch (Exception e) 
		{
			throw new Exception(String.format("数据:%s, 类型: %s, 无法转换成浮点数类型", this.value_, this.objType));
		}
	}
	/**
	 * 不带异常的转浮点数，注意不能转的全部是0
	 * @return
	 */
	public double safeDouble()
	{
		// bool 值返回0和1
		if(this.objType == LuneObjectType.BOOL)
		{
			if((boolean)this.value_) return 1;
			return 0;
		}
		try
		{
			return Double.parseDouble(this.strValue());
		}catch (Exception e) 
		{
			return 0; // 所有无法转的都会变成0；
		}
	}
	/**
	 * 转换成整数
	 * @return
	 * @throws Exception 
	 */
	public long longValue() throws Exception
	{
		// bool 值返回0和1
		if(this.objType == LuneObjectType.BOOL)
		{
			if((boolean)this.value_) return 1;
			return 0;
		}
		try
		{
			// 尝试进行转换
			double  v = this.doubleValue();
			return new Double(v).longValue(); // 这里不用直接用Long.parseLong, 如果是浮点数字符串直接报错。。。
		}catch (Exception e) 
		{
			throw new Exception(String.format("数据:%s, 类型: %s, 无法转换成整数类型", this.value_, this.objType));
		}
	}
	/**
	 * 不带异常的转整形，注意不能转的全部是0
	 * @return
	 */
	public long safeLong()
	{
		// bool 值返回0和1
				if(this.objType == LuneObjectType.BOOL)
				{
					if((boolean)this.value_) return 1;
					return 0;
				}
				try
				{
					// 尝试进行转换
					double  v = this.doubleValue();
					return new Double(v).longValue(); // 这里不用直接用Long.parseLong, 如果是浮点数字符串直接报错。。。
				}catch (Exception e) 
				{
					return 0;
				}
	}
	/**
	 * 转换成字符串
	 * @return
	 */
	public String strValue()
	{
		return String.valueOf(this.value_);
	}
	/**
	 * 重写toString方法，仅显示value_的内容。
	 */
	@Override
	public String toString() 
	{
		return String.valueOf(this.value_);
	}
	/**
	 * 转换成boolean值。 空值，boolean ，空字符串。 数字0.
	 * @return
	 * @throws Exception 
	 */
	public boolean toBool() throws Exception 
	{
		// 如果本身LuneObject的value是null对象，这里就直接返回false
		if(this.value_ == null) return false;
		
		if(this.objType == LuneObjectType.NUMBER)
			return this.doubleValue()  != 0.0;
		else if(this.objType == LuneObjectType.STRING)
			return this.strValue().length() > 0;
		else if(this.objType == LuneObjectType.BOOL)
			return (boolean) this.value_;
		else if(this.objType == LuneObjectType.None)
			return false;
		return true;
	}
	/**
	 * 比较运算
	 */
	public boolean equals(Object obj) 
	{
		if(obj instanceof LuneObject)
		{
			LuneObject obj_ = (LuneObject)obj;
			// 这里针对null进行了单独的特殊判断。只有全部为null才为true，否则任意为null则为false
			if(this.value_ == null && obj_.value_ == null)
			{
				return true;
			}
			if(this.value_ == null || obj_.value_ == null)
			{
				return false;
			}
			// 针对数字采样double进行比较。
			if(this.objType == LuneObjectType.NUMBER || obj_.objType == LuneObjectType.NUMBER)
				try
				{
					return this.doubleValue() == obj_.doubleValue();
				} catch (Exception e)
				{
					return false;
				}
			// 其他类型直接调用object的比较
			return this.value_.equals(obj_.value_);
		}
		return super.equals(obj);
	}
	/**
	 * 用于Map中作为key进行判断取值。 
	 * 在不重写情况下：两个LuneObject(2) 对象 返回false因为是2个不同的对象。
	 * 重写后：都使用value_的hash做判断所以就没有问题。
	 */
	@Override
	public int hashCode()
	{
		// 如果value_为空，这里是会有问题的。
		if(this.value_ == null)
		{
			// TODO 这里需要处理。
		}
		return this.value_.hashCode();
	}
	/**
	 * LuneObject本身不支持属性设置，需要继承后实现
	 * boolean, int, string, none这些将不支持属性操作。好像又不那么面向对象了。
	 * @param name
	 * @return
	 */
	public LuneObject GetAttribute(String name)
	{
		return LuneObject.noneLuneObject;
	}
	/**
	 * LuneObject本身不支持属性设置，需要继承后实现
	 * boolean, int, string, none这些将不支持属性操作。好像又不那么面向对象了。
	 * @param name
	 * @param value
	 */
	public void SetAttribute(String name, LuneObject value)
	{
		throw new NotImplementedException();
	}
	
	
}
