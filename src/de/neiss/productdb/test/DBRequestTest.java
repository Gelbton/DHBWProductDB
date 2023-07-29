package de.neiss.productdb.test;

import static org.junit.Assert.*;

import de.neiss.productdb.data.DBReader;
import de.neiss.productdb.model.Company;
import de.neiss.productdb.model.Person;
import de.neiss.productdb.model.Product;
import de.neiss.productdb.util.DBRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DBRequestTest
{

	DBReader dbReader = new DBReader("dbReaderTestDB.txt");
	DBRequest dbRequest = new DBRequest(dbReader);

	@Test
	public void testSearchPersonsById()
	{
		Person person = dbRequest.searchPersons(1);
		assertNotNull(person);
		assertEquals(1, person.getPersonId());
		assertEquals("Mike Houston", person.getName());
	}

	@Test
	public void testSearchPersonsByName()
	{
		ArrayList<Person> persons = dbRequest.searchPersons("Rafael");
		assertNotNull(persons);
		assertEquals("Incorrect matches!", "[Michael Rafael, Rafael Fowler]",
				persons.stream()
						.map(Person::getName)
						.toList()
						.toString());
	}


	@Test
	public void testSearchProductsByName()
	{
		ArrayList<Product> products = dbRequest.searchProducts("iPad");
		assertNotNull(products);
		assertEquals(2, products.size());
		assertEquals("Invalid Products", "[iPad, iPad Mini]", products.stream()
				.map(Product::getProductName)
				.toList()
				.toString());
	}

	@Test
	public void testSearchCompanyName() //checks if an empty String is returned if no company is found
	{
		String companyName = dbRequest.searchCompanyName(187);
		assertEquals("something was found even though nothing was there? How the hell did that happen? This should be" +
				" empty", "", companyName);
	}

	@Test
	public void testGetProductNetwork() //checks if the productNetwork only shows products not owned by the person
	{
		ArrayList<String> productNetwork = dbRequest.getProductNetwork(0);
		assertEquals(2, productNetwork.size());
		assertTrue(productNetwork.contains("iPad Mini"));
		assertTrue(productNetwork.contains("iPhone"));
		assertFalse(productNetwork.contains("iPad"));
		assertFalse(productNetwork.contains("Google Home"));
	}

	@Test
	public void testGetCompanyNetwork() //checks if the company network is empty if a person owns products from all companies
	{
		ArrayList<String> companyNetwork = dbRequest.getCompanyNetwork(0);
		assertEquals(1, companyNetwork.size());
		assertFalse(companyNetwork.contains("Apple"));
		assertFalse(companyNetwork.contains("Google"));
		assertEquals("[]", companyNetwork.toString());
	}
}
