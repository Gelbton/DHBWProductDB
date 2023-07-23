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

	public Person(int person_id, String name, String gender)
	{
		this.personId = person_id;
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

	public void addFriend(Person friend)
	{
		friends.add(friend);
	}

	public void removeFriend(Person friend)
	{
		friends.remove(friend);
	}

	public List<Product> getProductsOwned()
	{
		return productsOwned;
	}

	public void addProductOwned(Product product)
	{
		productsOwned.add(product);
	}

	public void removeProductOwned(Product product)
	{
		productsOwned.remove(product);
	}
}
