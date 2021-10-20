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
		ctx.execfile(args[0]);
	}

}
