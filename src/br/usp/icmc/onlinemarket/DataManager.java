package br.usp.icmc.onlinemarket;

import com.opencsv.CSVReader;
import com.sun.istack.internal.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class DataManager {

	private static DataManager instance = null;

	private ArrayList<User> userTable;
	private ArrayList<Product> productTable;

	public static DataManager getInstance(File directory)
		throws IllegalArgumentException {
		if (instance == null)
			instance = new DataManager(directory);
		return instance;
	}

	public static DataManager getInstance() {
		return instance;
	}

	private DataManager(
		@NotNull
		File directory
	) throws
	  IllegalArgumentException {

		if (directory.isDirectory()) throw new IllegalArgumentException();
		File usersFile;
		File productsFile;

		usersFile = new File(directory.getPath() + "/users.csv");
		productsFile = new File(directory.getPath() + "/products.csv");

		if (!usersFile.exists()) {
			try {
				usersFile.createNewFile();
			} catch (IOException e) {
				System.err.println(
					"Could not create file " + usersFile.getName() +
					". maybe you don't have permission to write in this folder"
				);
				System.exit(1);
			}
		}
		if (!productsFile.exists()) {
			try {
				productsFile.createNewFile();
			} catch (IOException e) {
				System.err.println(
					"Could not create file " + productsFile.getName() +
					". maybe you don't have permission to write in this folder"
				);
				System.exit(1);
			}
		}

		loadFromCsv(usersFile, productsFile);

	}

	private void loadFromCsv(File usersFile, File productsFile) {

		try {
			CSVReader csvReader = new CSVReader(new FileReader(usersFile));

			csvReader.forEach(
				tokens -> userTable.add(
					new User(
						tokens[0],
						tokens[1],
						tokens[2],
						tokens[3],
						tokens[4],
						Long.parseLong(tokens[5]),
						Long.parseLong(tokens[6])
					)
				)
			);

			csvReader = new CSVReader(new FileReader(productsFile));

			csvReader.forEach(
				tokens -> productTable.add(
					new Product(
						Long.parseLong(tokens[0]),
						tokens[1],
						Double.parseDouble(tokens[2]),
						tokens[3],
						Long.parseLong(tokens[4])
					)
				)
			);

		} catch (IOException e) {
			System.err.println(
				"something went wrong when reading from files," +
				" check if you have read access in this directory"
			);
			System.exit(1);
		}
	}

	public boolean verifyLogin(String userName, String passHash) {

		Optional<User> opUser = userTable.stream()
			.filter(user -> user.getName().equals(userName))
			.filter(user -> user.getPasswordMd5().equals(passHash))
			.findFirst();
		return opUser.isPresent();

	}

	public boolean addUser(
		String userName, String name, String email, String address,
		String passwordMd5, long phoneNumber, long id
	){
		Optional<User> opUser = userTable.stream()
			.filter(user -> user.getId() == id)
			.findFirst();
		if (opUser.isPresent()) return false;

		userTable.add(
			new User(
				userName,
			    name,
			    email,
			    address,
			    passwordMd5,
			    phoneNumber,
			    id
			)
		);

		return true;
	}
}
