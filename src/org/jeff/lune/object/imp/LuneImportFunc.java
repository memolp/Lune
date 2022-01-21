package org.jeff.lune.object.imp;

import java.io.FileNotFoundException;

import org.jeff.lune.LuneRuntime;
import org.jeff.lune.object.LuneExecuteable;
import org.jeff.lune.object.LuneObject;
import org.jeff.lune.object.LuneObjectType;

/**
 * 导入模块函数
 * 示例： import("test1") 注意和lua一样不需要输入后缀，但是需要括号。
 * @author 覃贵锋
 *
 */
public class LuneImportFunc extends LuneExecuteable
{
	public static String LUNE_FILE_EXT =  ".lune";
	
	public LuneImportFunc()
	{
		super();
	}
	@Override
	public LuneObject Execute(LuneRuntime rt, LuneObject... args) throws Exception
	{
		if(args.length != 1)
		{
			throw new Exception("import 方法仅支持一个参数");
		}
		LuneObject file = args[0];
		if(file.objType != LuneObjectType.STRING)
		{
			throw new Exception(String.format("import 后面是文件的字符串路径，提供的类型:%s 不被支持", file.objType));
		}
		String filename = file.strValue() + LUNE_FILE_EXT;
		try 
		{
			rt.execfile(filename);
		} catch (FileNotFoundException e) 
		{
			throw new Exception(String.format("文件路径:%s 未找到！", filename));
		}
		return LuneObject.noneLuneObject;
	}

}
