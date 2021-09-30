package main.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jeff.lune.token.Token;
import org.jeff.lune.token.TokenReader;

public class TestMain
{

	public static void main(String[] args) throws Exception
	{
		File file = new File("test1.lune");
		if(file.exists())
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			TokenReader tk = new TokenReader(reader, "test1.lua");
			List<Token> tokens = tk.tokenizer();
			for(Token t : tokens)
			{
				System.out.printf("%s\n", t.tokenStr);
			}
		}
	}

}
