package main.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jeff.lune.LuneContext;
import org.jeff.lune.parsers.SyntaxParser;
import org.jeff.lune.token.Token;
import org.jeff.lune.token.TokenReader;

public class TestMain
{

	public static void main(String[] args) throws Exception
	{
		LuneContext ctx = new LuneContext();
		ctx.execfile("test2.lune");
	}

}
