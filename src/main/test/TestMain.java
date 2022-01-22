package main.test;


import org.jeff.lune.LuneContext;


public class TestMain
{

	public static void main(String[] args) throws Exception
	{
		if(args.length < 1)
		{
			System.out.println("need filename");
			return;
		}
		LuneContext ctx = new LuneContext();
		String filename = null;
		int args_len = args.length;
		for(int i =0; i < args_len; i++)
		{
			String param = args[i];
			// 执行的文件
			if(param.equalsIgnoreCase("-file"))
			{
				if( i + 1 >= args_len)
				{
					System.err.println("-file 后面需要接filename参数");
					System.exit(1);
				}
				filename = args[i + 1];
				i += 1;
				continue;
			}
			// 开启调试模式
			if(param.equalsIgnoreCase("-debug"))
			{
				ctx.SetDebug(true);
			}
		}
		
		if(filename == null)
		{
			System.err.println("执行请增加参数 -file [filename]");
			System.exit(1);
		}
		ctx.execfile(filename);
	}

}
