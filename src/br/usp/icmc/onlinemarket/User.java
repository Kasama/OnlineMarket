package br.usp.icmc.onlinemarket;

import java.util.Observable;
import java.util.Observer;

public class User implements Observer {

	private static final String TYPECUSTOMER = "customer";
	private static final String TYPEPROVIDER = "provider";
	private static final String TYPEBOTH = "customerAndProvider";


	private String userName;
	private String name;
	private String email;
	private String address;
	private String passwordMd5;
	private String type;
	private boolean isCustomer;
	private boolean isProvider;
	private long phoneNumber;
	private long id;

	public User(
		String userName, String name, String email, String address,
		String passwordMd5, long phoneNumber, long id, String type
	) {
		this.userName = userName;
		this.name = name;
		this.email = email;
		this.address = address;
		this.passwordMd5 = passwordMd5;
		this.phoneNumber = phoneNumber;
		this.type = type;
		this.id = id;
		switch (type){
			case TYPECUSTOMER:
				isCustomer = true;
				isProvider = false;
				break;
			case TYPEPROVIDER:
				isCustomer = false;
				isProvider = true;
				break;
			case TYPEBOTH:
				isCustomer = true;
				isProvider = true;
				break;
		}
	}

	public String getUserName() {
		return userName;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public String getPasswordMd5() {
		return passwordMd5;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	@Override
	public void update(Observable o, Object arg) {

	}

	public boolean isCustomer() {
		return isCustomer;
	}

	public boolean isProvider() {
		return isProvider;
	}
}
