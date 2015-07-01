package br.usp.icmc.onlinemarket;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class Product extends Observable {

	long id;
	String name;
	double price;
	String bestBefore;
	long amount;
	long provider;

	Vector<Observer> obs;

	public Product(
		long id, String name, double price, String bestBefore, long amount
	) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.bestBefore = bestBefore;
		this.amount = amount;
		this.obs = new Vector<>();
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

	public boolean isUnavailable(){
		return !(amount > 0);
	}

	public boolean isAvailable(){
		return amount > 0;
	}

	public long getAmount() {
		return amount;
	}

	public Observer[] getObservers(){
		return (Observer[]) obs.toArray();
	}

	@Override
	public synchronized void addObserver(Observer o) {
		super.addObserver(o);
		obs.add(o);
	}

	@Override
	public synchronized void deleteObserver(Observer o) {
		super.deleteObserver(o);
		obs.remove(o);
	}

	@Override
	public synchronized void deleteObservers() {
		super.deleteObservers();
		obs.removeAllElements();
	}

	public long getProvider() {
		return provider;
	}
}