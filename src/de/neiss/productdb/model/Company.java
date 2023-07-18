package de.neiss.productdb.model;

import java.util.ArrayList;
import java.util.List;

public class Company
{
	private int companyId;
	private String companyName;
	private List<Product> productsCreated;

	public Company(int company_id, String companyName)
	{
		this.companyId = company_id;
		this.companyName = companyName;
		this.productsCreated = new ArrayList<>();
	}

	public int getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(int companyId)
	{
		this.companyId = companyId;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public List<Product> getProductsCreated()
	{
		return productsCreated;
	}

	public void addProductCreated(Product product)
	{
		productsCreated.add(product);
	}

	public void removeProductCreated(Product product)
	{
		productsCreated.remove(product);
	}
}
