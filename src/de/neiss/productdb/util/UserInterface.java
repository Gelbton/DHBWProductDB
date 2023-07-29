package de.neiss.productdb.util;

import de.neiss.productdb.data.DBReader;
import de.neiss.productdb.model.Person;
import de.neiss.productdb.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UserInterface
{
	private DBReader reader = new DBReader("productproject2023.txt");
	private DBRequest request = new DBRequest(reader);


	public UserInterface()
	{

	}

	/**
	 * sends a request to the database with the given input, the response is printed to out and not returned
	 * @param args input command that will be processed
	 */
	public void start(String args)
	{
			String input = args;
			int argumentIndex = input.indexOf("=");

			ArrayList<String> result = new ArrayList<>();

			String command = null;
			String argument = null;
			if (input.equals("exit") || input.equals("--exit"))
				{
				System.out.println("\nTerminating program!");
				} else if (argumentIndex != -1)
				{
				command = input.substring(0, argumentIndex);
				argument = input.substring(argumentIndex + 1).replaceAll("\"", "");
				} else
				{
				command = input;
				}

			switch (command)
				{
				case "--personensuche":
					result = ( ArrayList<String> ) request.searchPersons(argument).stream()
							.map(Person::getName)
							.collect(Collectors.toList());
					break;
				case "--produktsuche":
					result = ( ArrayList<String> ) request.searchProducts(argument).stream()
							.map(Product::getProductName)
							.collect(Collectors.toList());
					break;
				case "--produktnetzwerk":
					int id = Integer.parseInt(argument);
					result = request.getProductNetwork(id);
					break;
				case "--firmennetzwerk":
					id = Integer.parseInt(argument);
					result = request.getCompanyNetwork(id);
					break;
				default:
					System.out.println("unknown command");
				}
			printResponse(result);
			
	}

	/**
	 * Prints the String Arraylist to the output, sorting it alphabetically and separating each list Element with a ", "
	 * @param list the list that will be printed in alphabetical order
	 */
	private void printResponse(ArrayList<String> list) {
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		String response = String.join(", ", list);
		System.out.println(response);
	}

}
