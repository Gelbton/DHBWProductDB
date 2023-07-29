package de.neiss.productdb.model;

import java.util.ArrayList;
import java.util.List;

public class Company
{
	private int companyId;
	private String companyName;
	private final List<Product> productsCreated;

	public Company(int companyId, String companyName)
	{
		this.companyId = companyId;
		this.companyName = companyName;
		this.productsCreated = new ArrayList<>();
	}

	public int getCompanyId()
	{
		return companyId;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	/**
	 * Adds a product to the product-lineup of a company
	 * @param product Product that the company produces
	 */
	public void addProductCreated(Product product)
	{
		productsCreated.add(product);
	}

}
