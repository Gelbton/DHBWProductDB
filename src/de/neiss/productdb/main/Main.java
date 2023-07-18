package de.neiss.productdb.main;

import de.neiss.productdb.data.DBReader;
import de.neiss.productdb.model.Person;
import de.neiss.productdb.test.ProjektTester;
import de.neiss.productdb.util.DBRequest;
import de.neiss.productdb.util.UserInterface;

import java.util.ArrayList;

public class Main
{
	public static void main(String[] args)
	{
		UserInterface userInterface = new UserInterface();
		userInterface.start();
	}
}