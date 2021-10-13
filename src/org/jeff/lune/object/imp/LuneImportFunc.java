package org.jeff.lune.object.imp;

import java.io.FileNotFoundException;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

public class LuneImportFunc extends LuneExecuteable
{
	public LuneImportFunc()
	{
		super();
	}
	@Override
	public LuneObject Execute(LuneRuntime rt, LuneObject... args) 
	{
		if(args.length != 1) throw new RuntimeException();
		LuneObject file = args[0];
		if(file.objType != LuneObjectType.STRING) throw new RuntimeException();
		
		String filename = file.toString() + ".lune";
		try {
			rt.execfile(filename);
		} catch (FileNotFoundException e) 
		{
			throw new RuntimeException();
		}
		return null;
	}

}
