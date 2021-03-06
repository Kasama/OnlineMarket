package br.usp.icmc.onlinemarket;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.sun.istack.internal.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
						Long.parseLong(tokens[6]),
					    tokens[7]
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

	public void writeToCsv(File usersFile, File productsFile){

		try {

			CSVWriter csvWriter = new CSVWriter(new FileWriter(usersFile));

			List<String[]> toWrite = new ArrayList<>();
			toWrite = userTable.stream()
				.map(
					user -> {
					    String[] str = new String[7];
					    str[0] = Long.toString(user.getId());
					    str[1] = user.getName();
					    str[2] = user.getPasswordMd5();
					    str[3] = user.getAddress();
					    str[4] = user.getEmail();
					    str[5] = user.getUserName();
					    str[6] = Long.toString(user.getPhoneNumber());
					    return str;
				    }
				).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean addUser(
		String userName, String name, String email, String address,
		String passwordMd5, long phoneNumber, long id, String type
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
			    id,
			    type
			)
		);

		return true;
	}
}