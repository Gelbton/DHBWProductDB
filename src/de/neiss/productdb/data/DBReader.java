package de.neiss.productdb.data;

import de.neiss.productdb.model.Company;
import de.neiss.productdb.model.Person;
import de.neiss.productdb.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class DBReader
{
	private final String dataSetPath;

	private final ArrayList<Company> companies = new ArrayList<>();
	private final ArrayList<Person> persons = new ArrayList<>();
	private final ArrayList<Product> products = new ArrayList<>();
	private final HashSet<Integer> processedIds = new HashSet<>();

	/**
	 * Constructs a DBReader with the given data set path.
	 *
	 * @param dataSetPath The file path to the data set.
	 */
	public DBReader(String dataSetPath)
	{
		this.dataSetPath = dataSetPath;
	}

	public ArrayList<Company> getCompanies()
	{
		return companies;
	}

	public ArrayList<Person> getPersons()
	{
		return persons;
	}

	public ArrayList<Product> getProducts()
	{
		return products;
	}

	/**
	 * Reads the data from the given file and stores them inside the DBreader.
	 */
	public void readData()
	{
		try (BufferedReader reader = new BufferedReader(new FileReader(dataSetPath)))
			{
			String line;
			String entityType = null;

			while ((line = reader.readLine()) != null)
				{
				line = line.trim();
				if (!line.isEmpty())
					{
					if (line.startsWith("New_Entity"))
						{
						entityType = determineEntityType(line);
						} else
						{
						createEntity(line, entityType);
						}
					}
				}
			} catch (IOException e)
			{
			e.printStackTrace();
			}
	}

	/**
	 * Determines the entity type based on the given line by comparing parameters.
	 *
	 * @param line The input line containing the entity information.
	 * @return The entity type as a string, or null if it cannot be determined.
	 */
	private String determineEntityType(String line)
	{
		String entityParams = line.substring(line.indexOf(":") + 1).trim();
		String[] params = entityParams.split(",");
		String[] parameterNames = new String[params.length];
		for (int i = 0; i < params.length; i++)
			{
			parameterNames[i] = params[i].replaceAll("\"", "").trim();
			}
		if (parameterNames.length == 3)
			{
			return "Person";
			} else if (parameterNames[0].equals("product_id") && parameterNames[1].equals("product_name"))
			{
			return "Product";
			} else if (parameterNames[0].equals("company_id") && parameterNames[1].equals("company_name"))
			{
			return "Company";
			} else if (parameterNames[0].equals("person1_id") && parameterNames[1].equals("person2_id"))
			{
			return "Friendship";
			} else if (parameterNames[0].equals("person_id") && parameterNames[1].equals("product_id"))
			{
			return "Ownership";
			} else if (parameterNames[0].equals("product_id") && parameterNames[1].equals("company_id"))
			{
			return "Producer";
			}

		return null;
	}

	/**
	 * Creates an entity based on the given data and entity type.
	 *
	 * @param line       The input line containing entity data.
	 * @param entityType The type of entity being created.
	 */
	private void createEntity(String line, String entityType)
	{
		String[] data = line.split(",");
		int expectedArgs = getExpectedArgsCount(entityType);
		if (data.length != expectedArgs)
			{
			return;
			}
		switch (entityType)
			{
			case "Person":
				int personId = Integer.parseInt(getRawInfo(data[0]));
				if (!processedIds.add(personId))
					{
					return;
					}
				String personName = getRawInfo(data[1]);
				String personGender = getRawInfo(data[2]);
				Person person = new Person(personId, personName, personGender);
				persons.add(person);
				break;
			case "Company":
				int companyId = Integer.parseInt(getRawInfo(data[0]));
				if (!processedIds.add(companyId))
					{
					return;
					}
				String companyName = getRawInfo(data[1]);
				Company company = new Company(companyId, companyName);
				companies.add(company);
				break;
			case "Product":
				int productId = Integer.parseInt(getRawInfo(data[0]));
				if (!processedIds.add(productId))
					{
					return;
					}
				String productName = getRawInfo(data[1]);
				Product product = new Product(productId, productName);
				products.add(product);
				break;
			case "Friendship":
				int person1Id = Integer.parseInt(getRawInfo(data[0]));
				int person2Id = Integer.parseInt(getRawInfo(data[1]));
				Person friend1 = searchPerson(person1Id);
				Person friend2 = searchPerson(person2Id);
				if (friend1 != null && friend2 != null)
					{
					friend1.addFriend(friend2);
					friend2.addFriend(friend1);
					}
				break;
			case "Ownership":
				int ownerId = Integer.parseInt(getRawInfo(data[0]));
				productId = Integer.parseInt(getRawInfo(data[1]));
				Person owner = searchPerson(ownerId);
				Product ownedProduct = searchProduct(productId);
				if (owner != null && ownedProduct != null)
					{
					owner.addProductOwned(ownedProduct);
					}
				break;
			case "Producer":
				int producerProductId = Integer.parseInt(getRawInfo(data[0]));
				int producerCompanyId = Integer.parseInt(getRawInfo(data[1]));
				Product producedProduct = searchProduct(producerProductId);
				Company producingCompany = searchCompany(producerCompanyId);
				if (producedProduct != null && producingCompany != null)
					{
					producedProduct.setCompanyId(producingCompany.getCompanyId());
					producingCompany.addProductCreated(producedProduct);
					}
				break;
			default:
				break;
			}
	}

	/**
	 * internal function to find correct amount of arguments, necessary to detect invalid entries
	 * @param entityType EntityType of which the argument count needs to be determined
	 * @return Returns the amount of arguments
	 */
	private int getExpectedArgsCount(String entityType)
	{
		switch (entityType)
			{
			case "Person":
				return 3;
			case "Company":
				return 2;
			case "Product":
				return 2;
			case "Friendship":
				return 2;
			case "Ownership":
				return 2;
			case "Producer":
				return 2;
			default:
				return 0; // Return 0 or throw an exception for unknown entity types.
			}
	}

	/**
	 * Removes all the unnecessary spaces from a line and removes quotation marks
	 * @param line line to be modified
	 * @return The passed line in a format that is easier to process
	 */
	private String getRawInfo(String line)
	{
		return line.replaceAll("\"", "").trim();
	}

	/**
	 * Searches for a person with the specified ID in the list of persons.
	 *
	 * @param personId The ID of the person to search for.
	 * @return The Person object with the specified ID, or null if not found.
	 */
	private Person searchPerson(int personId)
	{
		return persons.stream()
				.filter(person -> person.getPersonId() == personId)
				.findFirst()
				.orElse(null);
	}
	/**
	 * Searches for a product with the specified ID in the list of products.
	 *
	 * @param productId The ID of the product to search for.
	 * @return The Product object with the specified ID, or null if not found.
	 */
	private Product searchProduct(int productId)
	{
		return products.stream()
				.filter(product -> product.getProductId() == productId)
				.findFirst()
				.orElse(null);
	}
	/**
	 * Searches for a company with the specified ID in the list of companies.
	 *
	 * @param companyId The ID of the company to search for.
	 * @return The Company object with the specified ID, or null if not found.
	 */
	private Company searchCompany(int companyId)
	{
		return companies.stream()
				.filter(company -> company.getCompanyId() == companyId)
				.findFirst()
				.orElse(null);
	}
}
