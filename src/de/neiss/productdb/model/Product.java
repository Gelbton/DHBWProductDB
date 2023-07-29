package de.neiss.productdb.model;

public class Product
{
	private int productId;
	private String productName;
	private int companyId;

	public Product(int productId, String productName)
	{
		this.productId = productId;
		this.productName = productName;
	}

	public int getProductId()
	{
		return productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setCompanyId(int companyId)
	{
		this.companyId = companyId;
	}

	public int getCompanyId()
	{
		return companyId;
	}
}
