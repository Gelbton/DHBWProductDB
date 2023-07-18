package de.neiss.productdb.util;

import de.neiss.productdb.model.Person;
import de.neiss.productdb.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UserInterface
{
	DBRequest request = new DBRequest();
	private final Scanner scanner;

	public UserInterface()
	{
		scanner = new Scanner(System.in);
	}

	public void start()
	{
		while (true)
			{
			System.out.print("Enter a command: ");
			String input = scanner.nextLine();
			int argumentIndex = input.indexOf("=");

			ArrayList<String> result = new ArrayList<>();

			String command;
			String argument = null;
			if (input.equals("exit") || input.equals("--exit"))
				{
				System.out.println("\nTerminating program!");
				break;
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
	}

	void printResponse(ArrayList<String> list) {
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		String response = String.join(", ", list);
		System.out.println(response);
	}

}
