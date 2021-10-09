package org.jeff.lune.object;

public class LuneStringModule extends LuneObject
{
	public LuneStringModule()
	{
		this.objType = LuneObjectType.OBJECT;
		this.SetAttribute("format", new LuneStringFormatFunc());
	}
}
