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
		//long cur = System.currentTimeMillis();
		LuneContext ctx = new LuneContext();
		ctx.execfile(args[0]);
		//long end = System.currentTimeMillis();
		//System.out.printf("cost: ---- %s ms\n", end - cur);
	}

}
