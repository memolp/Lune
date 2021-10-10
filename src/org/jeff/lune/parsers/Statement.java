package org.jeff.lune.parsers;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneObject;

public abstract class Statement
{
	public StatementType statementType;
	public int startLine;
	public int startColoumn;
	/** 标记为整体 */
	public boolean isWhole = false;
	/** 执行语句 */
	public abstract  LuneObject OnExecute(LuneRuntime rt, LuneObject object);
}
