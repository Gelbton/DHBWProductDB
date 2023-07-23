package de.neiss.productdb.data;

import de.neiss.productdb.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DBReader
{
	private String dataSetPath;

	private ArrayList<Company> companies = new ArrayList<>();
	private ArrayList<Person> persons = new ArrayList<>();
	private ArrayList<Product> products = new ArrayList<>();

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

	private void createEntity(String line, String entityType)
	{
		String[] data = line.split(",");
		switch (entityType)
			{
			case "Person":
				int personId = Integer.parseInt(getRawInfo(data[0]));
				String personName = getRawInfo(data[1]);
				String personGender = getRawInfo(data[2]);
				Person person = new Person(personId, personName, personGender);
				persons.add(person);
				break;
			case "Company":
				int companyId = Integer.parseInt(getRawInfo(data[0]));
				String companyName = getRawInfo(data[1]);
				Company company = new Company(companyId, companyName);
				companies.add(company);
				break;
			case "Product":
				int productId = Integer.parseInt(getRawInfo(data[0]));
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

	private String getRawInfo(String line)
	{
		return line.replaceAll("\"", "").trim();
	}

	private Person searchPerson(int personId)
	{
		return persons.stream()
				.filter(person -> person.getPersonId() == personId)
				.findFirst()
				.orElse(null);
	}

	private Product searchProduct(int productId)
	{
		return products.stream()
				.filter(product -> product.getProductId() == productId)
				.findFirst()
				.orElse(null);
	}

	private Company searchCompany(int companyId)
	{
		return companies.stream()
				.filter(company -> company.getCompanyId() == companyId)
				.findFirst()
				.orElse(null);
	}
}
