package br.usp.icmc.onlinemarket;

public class User {

	private String userName;
	private String name;
	private String email;
	private String address;
	private String passwordMd5;
	private long phoneNumber;
	private long id;

	public User(
		String userName, String name, String email, String address,
		String passwordMd5, long phoneNumber, long id
	) {
		this.userName = userName;
		this.name = name;
		this.email = email;
		this.address = address;
		this.passwordMd5 = passwordMd5;
		this.phoneNumber = phoneNumber;
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
}
