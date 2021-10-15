package org.jeff.lune;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.object.LuneClassFunc;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.imp.LuneImportFunc;
import org.jeff.lune.object.imp.LuneMathModule;
import org.jeff.lune.object.imp.LunePrintFunc;
import org.jeff.lune.object.imp.LuneRangeFunc;
import org.jeff.lune.object.imp.LuneStringModule;
import org.jeff.lune.object.imp.LuneTableModule;
import org.jeff.lune.parsers.SyntaxParser;
import org.jeff.lune.parsers.exps.BlockStatementType;
import org.jeff.lune.parsers.exps.IndexExpression;
import org.jeff.lune.parsers.exps.MemberExpression;
import org.jeff.lune.parsers.exps.ProgramStatement;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;
import org.jeff.lune.parsers.objs.IdentifierStatement;
import org.jeff.lune.parsers.objs.NumberStatement;
import org.jeff.lune.parsers.objs.StringStatement;

/**
 * Lune运行时
 * 1. 保存全局变量和当前变量
 * 2. 保存当前执行的代码模块
 * 3. 记录实时执行标记等
 * @author 覃贵锋
 *
 */
public class LuneRuntime 
{
	/** 全局变量命名空间 */
	LuneNamespace mGlobalNamespaces;
	/** 当前的命名空间 */
	LuneNamespace mCurrentNamespaces;
	/**
	 * 创建运行时，会注册各种内置的公用方法和接口
	 */
	public LuneRuntime()
	{
		mGlobalNamespaces = new LuneNamespace(LuneNamespaceType.GLOBAL);
		mGlobalNamespaces.AddSymbol("class", new LuneClassFunc());	// 声明类的关键字
		mGlobalNamespaces.AddSymbol("print", new LunePrintFunc());	// 打印日志输出
		mGlobalNamespaces.AddSymbol("import", new LuneImportFunc()); // 导入文件
		mGlobalNamespaces.AddSymbol("range", new LuneRangeFunc());  // 类似py的rang
		mGlobalNamespaces.AddSymbol("string", new LuneStringModule()); //字符串模块
		mGlobalNamespaces.AddSymbol("table", new LuneTableModule());  // table模块
		mGlobalNamespaces.AddSymbol("math", new LuneMathModule()); // math模块
		mCurrentNamespaces = mGlobalNamespaces;
	}
	/**
	 * 获取当前的命名空间
	 * @return
	 */
	public LuneNamespace CurrentNamespace()
	{
		return mCurrentNamespaces;
	}
	/**
	 * 通过语句获取变量
	 * @param state
	 * @param object
	 * @return
	 */
	public LuneObject GetLuneObject(Statement state, LuneObject object)
	{
		// 如果是变量标识， 那么要么是全局/局部变量 要么是object的属性变量
		if(state.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement idt = (IdentifierStatement)state;
			if(object == null)
			{	// 去命名空间找-这里找到的要么是全局的，要么是局部的（函数内部的）
				return mCurrentNamespaces.GetSymbol(idt.name);
			}else
			{  // object的属性变量
				return object.GetAttribute(idt.name);
			}
		}
		// 属性访问类型obj.vals
		/*else if(state.statementType == StatementType.MEMBER)
		{
			MemberExpression mem = (MemberExpression)state;
			return this.GetMember(mem, object);
		}else if(state.statementType == StatementType.INDEX)
		{
			IndexExpression idx = (IndexExpression)state;
			return this.GetLuneObject(idx.object, object);
		}*/
		// 数字
		else if(state.statementType == StatementType.NUMBER)
		{
			return new LuneObject(((NumberStatement)state).value);
		}else if(state.statementType == StatementType.STRING)
		{
			return new LuneObject(((StringStatement)state).value);
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
	public LuneObject GetMember(MemberExpression exp, LuneObject object)
	{
		if(exp.parent.statementType != StatementType.IDENTIFIER)
			throw new RuntimeException();
		
		LuneObject parent = null;
		IdentifierStatement idt = (IdentifierStatement)exp.parent;
		if(object == null)
		{
			parent = mCurrentNamespaces.GetSymbol(idt.name);
		}else
		{
			parent = object.GetAttribute(idt.name);
		}
		if(parent == null)
			throw new RuntimeException();
		if(exp.child.statementType == StatementType.IDENTIFIER)
			return parent.GetAttribute(((IdentifierStatement)exp.child).name);
		else if(exp.child.statementType == StatementType.MEMBER)
		{
			return this.GetMember((MemberExpression) exp.child, parent);
		}else if(exp.child.statementType == StatementType.FUNCCALL)
		{
			//return exp.child;
			return null;
		}
		else
		{
			throw new RuntimeException();
		}
	}

	
	public boolean IsBreakFlag = false;
	public boolean IsReturnFlag = false;
	public boolean IsContinueFlag = false;
	
	List<LuneNamespace> mNamespaceStack = new LinkedList<LuneNamespace>();
	public void PushNamespace(LuneNamespace namespace) 
	{
		mNamespaceStack.add(mCurrentNamespaces);
		mCurrentNamespaces = namespace;
	}

	public void PopNamespace() 
	{
		if(!mNamespaceStack.isEmpty())
			mCurrentNamespaces = mNamespaceStack.remove(mNamespaceStack.size() - 1);
	}

	/**
	 * 执行文件
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public LuneObject execfile(String filename) throws FileNotFoundException
	{
		// 创建Buffer执行Token解析和语法树构建
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		SyntaxParser parser = new SyntaxParser(reader, filename);
		parser.parser();
		
		ProgramStatement s = parser.GetProgram();
		return s.OnExecute(this, null);
	}
	
	public LuneObject execute(String script)
	{
		StringReader reader = new StringReader(script);
		SyntaxParser parser = new SyntaxParser(reader, "<STRING>");
		parser.parser();
		
		ProgramStatement s = parser.GetProgram();
		return s.OnExecute(this, null);
	}
	
	List<BlockStatementType> mBlockTypeStack = new LinkedList<BlockStatementType>();
	public void PushBlockType(BlockStatementType blockType) 
	{
		mBlockTypeStack.add(blockType);
	}

	public void PopBlockType() 
	{
		if(mBlockTypeStack.isEmpty()) return;
		mBlockTypeStack.remove(mBlockTypeStack.size() - 1);
	}

	public boolean isInBlock(BlockStatementType btype) 
	{
		for(int i= mBlockTypeStack.size() -1 ; i>=0; i--)
		{
			if(mBlockTypeStack.get(i) == btype) return true;
		}
		return false;
	}
	
}
