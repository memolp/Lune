package org.jeff.lune.parsers;

import java.util.LinkedList;
import java.util.List;

public class FunctionStatement extends Statement
{
	List<Statement> blocks_ = new LinkedList<Statement>();
	public FunctionStatement()
	{
		
	}
}
