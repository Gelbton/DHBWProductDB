package de.neiss.productdb.main;

import de.neiss.productdb.util.UserInterface;

public class Main
{
	public static void main(String[] args)
	{
		if (args.length == 1)
			{
			String parameter = args[0];
			UserInterface userInterface = new UserInterface();
			userInterface.start(parameter);
			}
	}
}