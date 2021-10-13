package org.jeff.lune.parsers.exps;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;
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
		
		// TODO 这里没有做类型检查，需要加上才好
		LuneObject res = new LuneObject();
		switch(this.opType)
		{
			// 四则混合运算
			case  OP_PLUS:
		 		res.SetValue(obj1.toNumber() + obj2.toNumber()); return res;
		 	case OP_MINUS:
		 		res.SetValue(obj1.toNumber() -  obj2.toNumber()); return res;
		 	case OP_MULTI:
		 		res.SetValue(obj1.toNumber() * obj2.toNumber()); return res;
		 	case OP_DIV:
		 		res.SetValue(obj1.toNumber() / obj2.toNumber()); return res;
		 	case OP_MOD:
		 		res.SetValue(obj1.toNumber() % obj2.toNumber()); return res;
		 	// 位运算 - 只能操作整形数
		 	case OP_BIT_AND:
		 		res.SetValue(obj1.toLong() & obj2.toLong());	 return res;
		 	case OP_BIT_OR:
		 		res.SetValue(obj1.toLong() | obj2.toLong());	 return res;
		 	case OP_BIT_LEFT:
		 		res.SetValue(obj1.toLong() << obj2.toLong()); return res;
		 	case OP_BIT_RIGHT:
		 		res.SetValue(obj1.toLong() >> obj2.toLong());  return res;
		 	case OP_BIT_XOR:
		 		res.SetValue(obj1.toLong() ^ obj2.toLong());  return res;
		 	// 比较运算符
		 	case OP_GT:
		 		res.SetValue(obj1.toNumber() > obj2.toNumber());
		 		return res;
		 	case OP_GE:
		 		res.SetValue(obj1.toNumber() >= obj2.toNumber());
		 		return res;
		 	case OP_LT:
		 		res.SetValue(obj1.toNumber() < obj2.toNumber());
		 		return res;
		 	case OP_LE:
		 		res.SetValue(obj1.toNumber() <= obj2.toNumber());
		 		return res;
		 	case OP_EQ:
		 		res.SetValue(obj1.equals(obj2));
		 		return res;
		 	case OP_NE:
		 		res.SetValue(!obj1.equals(obj2));
		 		return res;
		    // 连接运算符
		 	case OP_AND:
		 		if(obj1.toBool() && obj2.toBool())
		 			res.SetValue(true);
		 		else
		 			res.SetValue(false);
		 		return res;
		 	case OP_OR:
		 		if(obj1.toBool())
		 			res.SetValue(true);
		 		else if(obj2.toBool())
		 			res.SetValue(true);
		 		else
		 			res.SetValue(false);
		 		return res;
		 	default:
		 		throw new RuntimeException();
		}
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


