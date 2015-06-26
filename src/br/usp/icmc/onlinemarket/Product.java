package br.usp.icmc.onlinemarket;

public class Product {

	long id;
	String name;
	double price;
	String bestBefore;
	long amount;

	public Product(
		long id, String name, double price, String bestBefore, long amount
	) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.bestBefore = bestBefore;
		this.amount = amount;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getBestBefore() {
		return bestBefore;
	}

	public long getAmount() {
		return amount;
	}
}
