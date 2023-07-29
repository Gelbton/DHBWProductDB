package de.neiss.productdb.model;

import java.util.ArrayList;
import java.util.List;

public class Person
{
	private int personId;
	private String gender;
	private String name;

	private List<Person> friends;
	private List<Product> productsOwned;

	public Person(int personId, String name, String gender)
	{
		this.personId = personId;
		this.name = name;
		this.gender = gender;
		this.friends = new ArrayList<>();
		this.productsOwned = new ArrayList<>();
	}

	public int getPersonId()
	{
		return personId;
	}


	public String getName()
	{
		return name;
	}

	public List<Person> getFriends()
	{
		return friends;
	}

	/**
	 * Adds another person to the friends of the current person. Note that this automatically
	 * adds this relationship is mutual and the current person automatically appearing in the other ones
	 * friend list as well!
	 * @param friend Person that is a friend
	 */
	public void addFriend(Person friend)
	{
		if (!this.friends.contains(friend))
			{
			friends.add(friend);
			}
	}

	public List<Product> getProductsOwned()
	{
		return productsOwned;
	}

	/**
	 * Adds the product to the products owned by the person. Does nothing if the person already owns the product.
	 * @param product new product
	 */
	public void addProductOwned(Product product)
	{
		if (!this.productsOwned.contains(product))
			{
			productsOwned.add(product);
			}
	}

}
