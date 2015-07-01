package br.usp.icmc.onlinemarket;

import java.util.Observable;
import java.util.Observer;

public class User implements Observer {

	private String userName;
	private String name;
	private String email;
	private String address;
	private String passwordMd5;
	private String type;
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
}
