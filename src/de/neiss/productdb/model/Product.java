package de.neiss.productdb.model;

public class Product
{
	private int productId;
	private String productName;
	private int companyId;

	public Product(int product_id, String product_name)
	{
		this.productId = product_id;
		this.productName = product_name;
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
