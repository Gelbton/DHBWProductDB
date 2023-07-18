package de.neiss.productdb.util;

import de.neiss.productdb.data.DBReader;
import de.neiss.productdb.model.Company;
import de.neiss.productdb.model.Person;
import de.neiss.productdb.model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class DBRequest
{
	private final DBReader reader = new DBReader("productproject2023.txt");
	private final ArrayList<Person> persons = reader.getPersons();
	private final ArrayList<Product> products = reader.getProducts();
	private final ArrayList<Company> companies = reader.getCompanies();

	public DBRequest()
	{
		reader.readData();
	}

	public Person searchPersons(int id)
	{
		return persons.stream()
				.filter(person -> person.getPersonId() == id)
				.findFirst()
				.orElse(null);
	}

	public ArrayList<Person> searchPersons(String name)
	{
		return persons.stream()
				.filter(person -> person.getName().toLowerCase().contains(name.toLowerCase()))
				.sorted(Comparator.comparing(Person::getName)) // Sort alphabetically by name
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public Product searchProducts(int id)
	{
		return products.stream()
				.filter(product -> product.getProductId() == id)
				.findFirst()
				.orElse(null);
	}

	public ArrayList<Product> searchProducts(String name)
	{
		return products.stream()
				.filter(product -> product.getProductName().toLowerCase().contains(name.toLowerCase()))
				.sorted(Comparator.comparing(Product::getProductName)) // Sort alphabetically by product name
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public String searchCompanyName(int id)
	{
		return companies.stream()
				.filter(company -> company.getCompanyId() == id)
				.map(Company::getCompanyName)
				.findFirst()
				.orElse("");
	}

	public ArrayList<String> getProductNetwork(int personId)
	{
		Person person = searchPersons(personId);
		if (person == null)
			{
			return new ArrayList<>();
			}

		ArrayList<String> productNetwork = person.getFriends().stream()
				.flatMap(friend -> friend.getProductsOwned().stream())
				.filter(product -> !person.getProductsOwned().contains(product))
				.map(Product::getProductName)
				.distinct()
				.sorted()
				.collect(Collectors.toCollection(ArrayList::new));

		return productNetwork;
	}

	public ArrayList<String> getCompanyNetwork(int personId)
	{
		Person person = searchPersons(personId);
		if (person == null)
			{
			return new ArrayList<>();
			}

		ArrayList<String> companyNetwork = person.getFriends().stream()
				.flatMap(friend -> friend.getProductsOwned().stream())
				.filter(product -> !person.getProductsOwned().contains(product))
				.map(Product::getCompanyId)
				.distinct()
				.sorted()
				.map(this::searchCompanyName)
				.collect(Collectors.toCollection(ArrayList::new));

		return companyNetwork;
	}
}
