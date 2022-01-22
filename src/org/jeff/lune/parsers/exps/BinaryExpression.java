package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;
import org.jeff.lune.token.TokenType;

/**
 * 二元表达式
 * @author 覃贵锋
 *
 */
public class BinaryExpression extends ExpressionStatement 
{
	/** 运算符类型 */
	public TokenType opType;
	/** 运算符 */
	public String op;
	/** 优先级-越小越优先 */
	public int opValue;
	/** 左操作数 */
	public Statement left;
	/** 右操作数 */
	public Statement right;
	/**
	 * 二元（双目）运算
	 * @param type
	 * @param opn
	 * @param line
	 * @param col
	 */
	public BinaryExpression(TokenType type, String opn, int line, int col)
	{
		super(StatementType.EXPRESSION_BINARY, line, col);
		// 这里会根据操作的类型计算优先级
		this.opType = type;
		this.op = opn;
		switch(type)
		{
			case OP_BIT_NOT:
			case OP_NOT:
				this.opValue = 2;
				break;
			case OP_PLUS:
			case OP_MINUS:
				this.opValue = 4;
				break;
			case OP_MULTI:
			case OP_DIV:
			case OP_MOD:
				this.opValue = 3;
				break;
			case OP_BIT_LEFT:
			case OP_BIT_RIGHT:
				this.opValue = 5;
				break;
			case OP_LE:
			case OP_LT:
			case OP_GE:
			case OP_GT:
				this.opValue = 6;
				break;
			case OP_EQ:
			case OP_NE:
				this.opValue = 7;
			case OP_BIT_AND:
				this.opValue = 8;
				break;
			case OP_BIT_XOR:
				this.opValue = 9;
				break;
			case OP_BIT_OR:
				this.opValue = 10;
				break;
			case OP_AND:
				this.opValue = 11;
				break;
			case OP_OR:
				this.opValue = 12;
				break;
			default:
				this.opValue = 0;
		}
	}
	@Override
	public LuneObject OnExecute(LuneRuntime rt, LuneObject object) 
	{
		// 获取左右操作数的对象
		LuneObject obj1 = this.left.OnExecute(rt, null);
		LuneObject obj2 = this.right.OnExecute(rt, null);
		LuneObject result = LuneObject.noneLuneObject;
		try
		{
			// TODO 这里没有做类型检查，需要加上才好
			// TODO 这里每次都会创建一个对象 例如 1+2+3 会创建两个对象，而不是整个表达式一个对象。待完善。
			switch(this.opType)
			{
				// 四则混合运算
				case  OP_PLUS:
					// 支持字符串的加法
					if(obj1.objType == LuneObjectType.STRING || obj2.objType == LuneObjectType.STRING)
					{
						result = new LuneObject(obj1.strValue() + obj2.strValue());
						break;
					}
					result =  LuneObject.CreateDoubleObject(obj1.doubleValue() + obj2.doubleValue());
					break;
			 	case OP_MINUS:
			 		result = LuneObject.CreateDoubleObject(obj1.doubleValue() -  obj2.doubleValue());
					break;
			 	case OP_MULTI:
			 		result =  LuneObject.CreateDoubleObject(obj1.doubleValue() * obj2.doubleValue());
					break;
			 	case OP_DIV:
			 		result = LuneObject.CreateDoubleObject(obj1.doubleValue() / obj2.doubleValue());
					break;
			 	case OP_MOD:
			 		result = LuneObject.CreateDoubleObject(obj1.doubleValue() % obj2.doubleValue());
			 		break;
			 	// 位运算 - 只能操作整形数
			 	case OP_BIT_AND:
			 		result =  new LuneObject(obj1.longValue() & obj2.longValue());
			 		break;
			 	case OP_BIT_OR:
			 		result =  new LuneObject(obj1.longValue() | obj2.longValue());
			 		break;
			 	case OP_BIT_LEFT:
			 		result = new LuneObject(obj1.longValue() << obj2.longValue());
			 		break;
			 	case OP_BIT_RIGHT:
			 		result = new LuneObject(obj1.longValue() >> obj2.longValue());
			 		break;
			 	case OP_BIT_XOR:
			 		result = new LuneObject(obj1.longValue() ^ obj2.longValue());
			 		break;
			 	// 比较运算符
			 	case OP_GT:
			 		result = LuneObject.CreateBooleanObject(obj1.doubleValue() > obj2.doubleValue());
			 		break;
			 	case OP_GE:
			 		result = LuneObject.CreateBooleanObject(obj1.doubleValue() >= obj2.doubleValue());
			 		break;
			 	case OP_LT:
			 		result = LuneObject.CreateBooleanObject(obj1.doubleValue() < obj2.doubleValue());
			 		break;
			 	case OP_LE:
			 		result = LuneObject.CreateBooleanObject(obj1.doubleValue() <= obj2.doubleValue());
			 		break;
			 	case OP_EQ: // ==和 != 判断是支持字符串，数字，对象的，因此比较复杂点
			 		result = LuneObject.CreateBooleanObject(obj1.equals(obj2));
			 		break;
			 	case OP_NE:
			 		result =  LuneObject.CreateBooleanObject(!obj1.equals(obj2));
			 		break;
			    // 连接运算符
			 	case OP_AND:
			 		if(obj1.toBool() && obj2.toBool())
			 			result = LuneObject.trueLuneObject;
			 		else
			 			result = LuneObject.falseLuneObject;
			 		break;
			 	case OP_OR:
			 		if(obj1.toBool())
			 			result = LuneObject.trueLuneObject;
			 		else if(obj2.toBool())
			 			result = LuneObject.trueLuneObject;
			 		else
			 			result = LuneObject.falseLuneObject;
			 		break;
			 	default:
			 		rt.RuntimeError(this, "%s 不支持的运算方式", this.toString()); // 这里面需要处理一下异常后的堆栈情况。
			 		break;
			}
		}catch (Exception e) {
			rt.RuntimeError(this, "%s", e.getMessage());
		}
		return result;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.left.toString());
		sb.append(this.op);
		sb.append(this.right.toString());
		sb.append(")");
		return sb.toString();
	}
}


