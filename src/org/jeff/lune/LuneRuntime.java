package org.jeff.lune;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import org.jeff.lune.object.LuneClassFunc;
import org.jeff.lune.object.LuneImportFunc;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LunePrintFunc;
import org.jeff.lune.object.LuneRangeFunc;
import org.jeff.lune.object.LuneStringModule;
import org.jeff.lune.object.LuneTableModule;
import org.jeff.lune.parsers.BlockStatementType;
import org.jeff.lune.parsers.ProgramStatement;
import org.jeff.lune.parsers.Statement;
import org.jeff.lune.parsers.StatementType;
import org.jeff.lune.parsers.SyntaxParser;
import org.jeff.lune.parsers.exps.IndexExpression;
import org.jeff.lune.parsers.exps.MemberExpression;
import org.jeff.lune.parsers.objs.IdentifierStatement;
import org.jeff.lune.parsers.objs.NumberStatement;
import org.jeff.lune.parsers.objs.StringStatement;

public class LuneRuntime 
{
	LuneNamespace mGlobalNamespaces;
	LuneNamespace mCurrentNamespaces;
	public LuneRuntime()
	{
		mGlobalNamespaces = new LuneNamespace(LuneNamespaceType.GLOBAL);
		mGlobalNamespaces.AddSymbol("class", new LuneClassFunc());
		mGlobalNamespaces.AddSymbol("print", new LunePrintFunc());
		mGlobalNamespaces.AddSymbol("import", new LuneImportFunc());
		mGlobalNamespaces.AddSymbol("xrange", new LuneRangeFunc());
		mGlobalNamespaces.AddSymbol("string", new LuneStringModule());
		mGlobalNamespaces.AddSymbol("table", new LuneTableModule());
		mCurrentNamespaces = mGlobalNamespaces;
	}
	
	public LuneNamespace CurrentNamespace()
	{
		return mCurrentNamespaces;
	}

	public LuneObject GetLuneObject(Statement state, LuneObject object)
	{
		if(state.statementType == StatementType.IDENTIFIER)
		{
			IdentifierStatement idt = (IdentifierStatement)state;
			if(object == null)
			{
				return mCurrentNamespaces.GetSymbol(idt.name);
			}else
			{
				return object.GetAttribute(idt.name);
			}
		}else if(state.statementType == StatementType.MEMBER)
		{
			MemberExpression mem = (MemberExpression)state;
			return this.GetMember(mem, object);
		}else if(state.statementType == StatementType.INDEX)
		{
			IndexExpression idx = (IndexExpression)state;
			return this.GetLuneObject(idx.object, object);
		}else if(state.statementType == StatementType.NUMBER)
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
	public Object execfile(String filename) throws FileNotFoundException
	{
		// 创建Buffer执行Token解析和语法树构建
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		SyntaxParser parser = new SyntaxParser(reader, filename);
		parser.parser();
		
		ProgramStatement s = parser.GetProgram();
		s.OnExecute(this, null);
		 return null;
	}
	
	public Object execute(String script)
	{
		
			return null;
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
