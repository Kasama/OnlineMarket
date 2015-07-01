package br.usp.icmc.onlinemarket;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.sun.istack.internal.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataManager {

	private ArrayList<User> userTable;
	private ArrayList<Product> productTable;

	public DataManager(
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

			for (Product product : productTable){
				File file = new File(
					productsFile.getParent() + "/products/" + product.getId() +
					".csv"
				);
				if (file.exists()) {
					csvReader = new CSVReader(new FileReader(file));
					csvReader.forEach(
						tokens -> product.addObserver(
							this.getUserById(Integer.parseInt(tokens[0]))
						)
					);
				}
			}

		} catch (IOException e) {
			System.err.println(
				"something went wrong when reading from files," +
				" check if you have read access in this directory"
			);
			System.exit(1);
		}
	}

	private User getUserById(int id) {
		return userTable.stream()
			.filter( u -> u.getId() == id)
			.findFirst()
			.orElse(null);
	}

	public User verifyLogin(String userName, String passHash) {

		return userTable.stream()
			.filter(user -> user.getUserName().equals(userName))
			.filter(user -> user.getPasswordMd5().equals(passHash))
			.findFirst()
			.orElse(null);

	}

	public void writeToCsv(File usersFile, File productsFile){

		try {

			CSVWriter csvWriter = new CSVWriter(new FileWriter(usersFile));

			List<String[]> toWrite = new ArrayList<>();

			for (Product product : productTable){
				File file = new File(
					productsFile.getParent() + "/products/" + product.getId() +
					".csv"
				);
				if (file.exists()) {
					csvWriter = new CSVWriter(new FileWriter(file));

					User[] u = (User[]) product.getObservers();
					String[] s = new String[1];


				}
			}

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

	public List<Product> getAvailableProducts() {
		return productTable.stream()
			.filter(Product::isAvailable)
			.collect(Collectors.toList());
	}

	public List<Product> getUnavailableProducts() {
		return productTable.stream()
			.filter(Product::isUnavailable)
			.collect(Collectors.toList());
	}

	public List<Product> getAllProducts(){
		return productTable;
	}

	public User getUserByUsername(String username) {
		return userTable.stream()
			.filter(u -> u.getUserName().equals(username))
			.findFirst()
			.orElse(null);
	}

	public Product getProductById(int id) {
		return productTable.stream()
				.filter( u -> u.getId() == id)
				.findFirst()
				.orElse(null);
	}
}