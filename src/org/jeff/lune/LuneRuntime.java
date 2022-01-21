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
import org.jeff.lune.object.imp.LuneTimeModule;
import org.jeff.lune.parsers.SyntaxParser;
import org.jeff.lune.parsers.exps.BlockStatementType;
import org.jeff.lune.parsers.exps.Statement;
import org.jeff.lune.parsers.exps.StatementType;
import org.jeff.lune.parsers.objs.IdentifierStatement;

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
	static String LINE_END = System.getProperty("line.separator");
	/** 全局变量命名空间 */
	LuneNamespace mGlobalNamespaces;
	/** 当前的命名空间 */
	LuneNamespace mCurrentNamespaces;
	/**
	 * 创建运行时，会注册各种内置的公用方法和接口
	 */
	public LuneRuntime()
	{
		this.mDebug = false;
		this.CreateRuntimeNamespace();
	}
	/**
	 * 创建运行时，可以设置调试模式启动，这样堆栈信息才会有。
	 * @param debug
	 */
	public LuneRuntime(boolean debug)
	{
		this.mDebug = debug;
		this.CreateRuntimeNamespace();
	}
	/**
	 * 创建运行时命名空间
	 */
	void CreateRuntimeNamespace()
	{
		mGlobalNamespaces = new LuneNamespace(LuneNamespaceType.GLOBAL);  // 全局模块-符号变量定义
		mGlobalNamespaces.AddSymbol("class", new LuneClassFunc());	// 声明类的关键字
		mGlobalNamespaces.AddSymbol("print", new LunePrintFunc());	// 打印日志输出
		mGlobalNamespaces.AddSymbol("import", new LuneImportFunc()); // 导入文件
		mGlobalNamespaces.AddSymbol("range", new LuneRangeFunc());  // 类似py的range
		mGlobalNamespaces.AddSymbol("string", new LuneStringModule()); //字符串模块
		mGlobalNamespaces.AddSymbol("math", new LuneMathModule()); // math模块
		mGlobalNamespaces.AddSymbol("time",  new LuneTimeModule()); // time模块
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
	 * 执行lune文件，打开文件并进行词法解析，然后执行代码。【解析后执行】
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public LuneObject execfile(String filename) throws FileNotFoundException
	{
		// 创建Buffer执行Token解析和语法树构建
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		SyntaxParser parser = new SyntaxParser(this, reader, filename);
		parser.parser();
		return parser.Execute();
	}
	/**
	 * 执行lune脚本代码。【解析后执行】
	 * @param script
	 * @return
	 */
	public LuneObject execute(String script)
	{
		StringReader reader = new StringReader(script);
		SyntaxParser parser = new SyntaxParser(this, reader, "<STRING>");
		parser.parser();
		return parser.Execute();
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
		}else  // 继续执行语句，返回结果
		{
			return state.OnExecute(this,  null);
		}
	}
	/** 命名空间栈 目前只针对函数，类，全局进行建栈。用于隔开变量的作用区域 */
	List<LuneNamespace> mNamespaceStack = new LinkedList<LuneNamespace>();
	/**
	 * 将命名空间压入栈，新的命名空间最为当前空间，旧的空间放入堆栈，用于递归查找。
	 * @param namespace
	 */
	public void PushNamespace(LuneNamespace namespace) 
	{
		mNamespaceStack.add(mCurrentNamespaces);
		mCurrentNamespaces = namespace;
	}
	/**
	 * 弹出当前的命名空间，并将堆栈的最后一个作为当前最新的命名空间。
	 */
	public void PopNamespace() 
	{
		//安全判断 只要RT逻辑没有问题，不会出现弹出后成为空的情况。
		if(!mNamespaceStack.isEmpty()) //TODO 增加日志
			mCurrentNamespaces = mNamespaceStack.remove(mNamespaceStack.size() - 1);
	}
	/** break语句标记 - 用于控制代码执行流程 在循环里面判断是否需要退出*/
	public boolean IsBreakFlag = false;
	/** return语句标记 - 用于控制代码执行流程，在函数，循环，条件等等Block的执行中判断是否要退出*/
	public boolean IsReturnFlag = false;
	/** continue语句标记 - 用于控制代码执行流程，在循环里面判断是否跳过后面的代码*/
	public boolean IsContinueFlag = false;
	/** 代码块类型栈 - 用于区函数、条件、循环等等代码块的层级，方便判断break，return，continue */
	List<BlockStatementType> mBlockTypeStack = new LinkedList<BlockStatementType>();
	/**
	 * 将当前的代码块类型压入栈。
	 * @param blockType
	 */
	public void PushBlockType(BlockStatementType blockType) 
	{
		mBlockTypeStack.add(blockType);
	}
	/**
	 * 弹出当前的代码块类型
	 */
	public void PopBlockType() 
	{
		// 安全判断，理论上不会出现。
		if(mBlockTypeStack.isEmpty()) return;  // TODO 增加日志
		mBlockTypeStack.remove(mBlockTypeStack.size() - 1);
	}
	/**
	 * 判断指定的代码块类型在不在这个层级里面。
	 * 例如：如果break 出现在了if里面，那就需要往前找if是不是在循环里面。
	 * @param btype
	 * @return
	 */
	public boolean isInBlock(BlockStatementType btype) 
	{
		for(int i= mBlockTypeStack.size() -1 ; i>=0; i--)
		{
			if(mBlockTypeStack.get(i) == btype) return true;
		}
		return false;
	}
	/** 代码执行的堆栈记录-用于在出错时抛出详细的调用过程*/
	List<Statement> mRuntimeStatementStack = new LinkedList<Statement>();
	/** 代码执行堆栈仅调试模式才执行 */
	boolean mDebug = false;
	/**
	 * 进入执行语句，期间的全部异常错误都会有这个语句产生
	 * @param state
	 */
	public void EnterStatement(Statement state)
	{
		if(!mDebug) return;
		mRuntimeStatementStack.add(state);
	}
	/**
	 * 离开执行语句，传入的语句对象仅作为安全判断。
	 * @param state
	 */
	public void LeaveStatement(Statement state)
	{
		if(!mDebug) return;
		if(mRuntimeStatementStack.isEmpty()) return; //TODO 
		// 移除最后的语句
		if(state != mRuntimeStatementStack.remove(mRuntimeStatementStack.size() - 1))
		{
			//TODO
		}
	}
	
	public void RuntimeWarning(String message)
	{
		
	}
	/**
	 * 运行时异常
	 * @param fmt
	 * @param args
	 */
	public void RuntimeError(String fmt, Object...args)
	{
		throw new RuntimeException(String.format(fmt, args)); 
	}
	
	public void RuntimeError(Statement state, String fmt, Object...args)
	{
		String  msg = String.format(fmt, args);
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("运行异常: %s 文件:%s, 行:%s,", msg, Statement.CurrentFile, state.startLine));
//		sb.append(LINE_END);
//		Statement temp = null;
//		for(int i=mRuntimeStatementStack.size() -1; i >= 0;  i --)
//		{
//			temp = mRuntimeStatementStack.get(i);
//			sb.append(String.format("%s 行:%s", temp, temp.startLine));
//			sb.append(LINE_END);
//		}
		throw new RuntimeException(sb.toString());
	}
	/**
	 * 语法错误-将直接退出
	 * @param fmt
	 * @param args
	 */
	public void SyntaxError(String fmt, Object...args)
	{
		System.err.println(String.format(fmt, args));
		System.exit(1);
	}
	
}
