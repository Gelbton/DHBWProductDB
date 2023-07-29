package de.neiss.productdb.util;

import de.neiss.productdb.data.DBReader;
import de.neiss.productdb.model.Company;
import de.neiss.productdb.model.Person;
import de.neiss.productdb.model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class DBRequest {
	private final DBReader reader;
	private final ArrayList<Person> persons;
	private final ArrayList<Product> products;
	private final ArrayList<Company> companies;

	/**
	 * initializes a DBRequest object that can send various requests to the database
	 * @param reader This needs an instance of DBReader. The database referenced in this instance will be used for
	 *               requests
	 */
	public DBRequest(DBReader reader) {
		this.reader = reader;
		this.persons = reader.getPersons();
		this.products = reader.getProducts();
		this.companies = reader.getCompanies();
		reader.readData();
	}

	/**
	 * searches the person with the matching ID
	 * @param id ID of the person
	 * @return The Person as Object, or returns null if nothing is found
	 */
	public Person searchPersons(int id)
	{
		return persons.stream()
				.filter(person -> person.getPersonId() == id)
				.findFirst()
				.orElse(null);
	}

	/**
	 * searches for all persons in which the name occurs
	 * @param name Name or part of the name that is searched
	 * @return an ArrayList containing all matches, or an empty ArrayList if nothing is found
	 */
	public ArrayList<Person> searchPersons(String name)
	{
		return persons.stream()
				.filter(person -> person.getName().toLowerCase().contains(name.toLowerCase()))
				.sorted(Comparator.comparing(Person::getName)) // Sort alphabetically by name
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Searches a product by its id
	 * @param id id of the product
	 * @return the product with the matching id, or null if nothing is found
	 */
	public Product searchProducts(int id)
	{
		return products.stream()
				.filter(product -> product.getProductId() == id)
				.findFirst()
				.orElse(null);
	}

	/**
	 * searches product by name
	 * @param name name of the product or products
	 * @return ArrayList of products containing matches, or empty ArrayList if nothing is found
	 */
	public ArrayList<Product> searchProducts(String name)
	{
		return products.stream()
				.filter(product -> product.getProductName().toLowerCase().contains(name.toLowerCase()))
				.sorted(Comparator.comparing(Product::getProductName)) // Sort alphabetically by product name
				.collect(Collectors.toCollection(ArrayList::new));
	}
	/**
	 * Searches for the company name with the specified ID.
	 *
	 * @param id The ID of the company.
	 * @return The company name if found, or an empty string if nothing is found.
	 */
	public String searchCompanyName(int id)
	{
		return companies.stream()
				.filter(company -> company.getCompanyId() == id)
				.map(Company::getCompanyName)
				.findFirst()
				.orElse("");
	}

	/**
	 * Retrieves the product network for a given person ID.
	 * The product network consists of products owned by the person's friends but not by the person themselves.
	 *
	 * @param personId The ID of the person to search for.
	 * @return An ArrayList containing the names of products in the product network, or an empty ArrayList if nothing is found.
	 */
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
	/**
	 * Retrieves the company network for a given person ID.
	 * The company network consists of companies that produce products owned by the person's friends but not by the person themselves.
	 *
	 * @param personId The ID of the person to search for.
	 * @return An ArrayList containing the names of companies in the company network, or an empty ArrayList if nothing is found.
	 */
	public ArrayList<String> getCompanyNetwork(int personId)
	{
		Person person = searchPersons(personId);
		if (person == null)
			{
			return new ArrayList<>();
			}

		ArrayList<String> companyNetwork = person.getFriends().stream()
				.flatMap(friend -> friend.getProductsOwned().stream())
				.map(Product::getCompanyId)
				.filter(x -> !person.getProductsOwned().stream()
						.map(Product::getCompanyId)
						.collect(Collectors.toCollection(ArrayList::new))
						.contains(x))
				.distinct()
				.sorted()
				.map(this::searchCompanyName)
				.collect(Collectors.toCollection(ArrayList::new));

		return companyNetwork;
	}
}
