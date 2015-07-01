package br.usp.icmc.onlinemarket;

import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Market {

	private static Market instance = new Market();
	private DataManager dataManager;
	private SessionManager sessionManager;

	private Market() {
		dataManager = new DataManager();
		sessionManager = new SessionManager();
	}

	public String[] signUp(
		String username, String name, String address, long telephone,
		String email, long id, String password, String type
	) {
		String[] ret = new String[2];
		String passwordMD5;
		passwordMD5 = sessionManager.getMD5Sum(password);
		if (dataManager.addUser(
			username, name, email, address, passwordMD5, telephone, id, type
		)
			)
		{
			ret[0] = Boolean.toString(true);
			ret[1] = "";
		} else {
			ret[0] = Boolean.toString(false);
			ret[1] = "impossible to add this user";
		}

		return ret;
	}

	public String[] login(
		String username, String password
	) {
		String[] ret;
		ret = new String[1];
		ret[0] = "";
		User user;
		String token = sessionManager.getSessionToken(username, password);
		if ((user = dataManager.verifyLogin(username, token)) != null) {
			sessionManager.addSession(token, user);
			ret[0] = token;
		}

		return ret;
	}

	public String[] request(String mode) {
		String[] ret;
		List<Product> products;
		switch (mode) {
			case "available":
				products = dataManager.getAvailableProducts();
				break;
			case "unavailable":
				products = dataManager.getUnavailableProducts();
				break;
			case "all":
				products = dataManager.getAllProducts();
				break;
			default:
				products = Collections.emptyList();
		}

		ret = new String[products.size() * 5];

		int i = 0;

		for (Product p : products) {
			ret[i++] = String.valueOf(p.getId());
			ret[i++] = p.getName();
			ret[i++] = String.valueOf(p.getPrice());
			ret[i++] = p.getBestBefore();
			ret[i++] = String.valueOf(p.getAmount());
		}

		return ret;
	}

	public String[] subscribe(String token, String productId) {
		User user = sessionManager.getUserByToken(token);
		Product product = dataManager.getProductById(
			Long.parseLong
				(productId)
		);
		String ret[] = new String[1];

		if (user.isCustomer() && !product.isAvailable()) {
			product.addObserver(user);
			ret[0] = Boolean.toString(true);
		} else {
			ret[0] = Boolean.toString(false);
		}

		return ret;
	}

	public String[] buyProduct(
		String token, String[] productId, String[] amount
	) {
		User u = sessionManager.getUserByToken(token);
		String ret[] = new String[2];

		if (!u.isCustomer()) {
			ret[0] = Boolean.toString(false);
			ret[1] = "Permission denied.";
			return ret;
		}

		for (int i = 0; i < productId.length; i++) {
			if (Integer.parseInt(amount[i]) < 0)
				throw new IllegalArgumentException(
					"Negative amount cannot be bought"
				);
			Product p = dataManager.getProductById(Integer.parseInt(productId[i]));
			List<String> unavailableIds = new ArrayList<>(productId.length);
			if (p == null) {
				unavailableIds.add(productId[i]);
			} else {
				if (!p.isAvailable() && Integer.parseInt(amount[i]) > 0) {
					p.notifyProvider(dataManager.getUserById(p.getProvider()));
					unavailableIds.add(productId[i]);
				}else {
					p.dencreaseAmount(Integer.parseInt(amount[i]));
				}
			}
			if (unavailableIds.size() > 0){
				ret = new String[unavailableIds.size()];
				ret[0] = Boolean.toString(false);
				for (int j = 0; j < unavailableIds.size(); j++) {
					ret[j+1] = unavailableIds.get(j);
				}
			}else{
				ret = new String[1];
				ret[0] = Boolean.toString(true);
			}
		}

		return ret;
	}

	public String[] addProduct(
		String token, String[] id, String[] name, String[] price, String[]
		bestBefore, String[] amount
	) {
		User u = sessionManager.getUserByToken(token);
		String ret[] = new String[2];

		if (!u.isProvider()) {
			ret[0] = Boolean.toString(false);
			ret[1] = "Permission denied.";
			return ret;
		}

		for (int i = 0; i < id.length; i++) {
			if (Integer.parseInt(amount[i]) < 0)
				throw new IllegalArgumentException(
					"Negative amount cannot be added"
				);
			Product p = dataManager.getProductById(Integer.parseInt(id[i]));
			if (p == null) {
				ret[0] = Boolean.toString(
					dataManager.addProduct(
						Long.parseLong(id[i]),
						name[i], Double.parseDouble(price[i]),
						bestBefore[i], Long.parseLong(amount[i]),
						u.getId()
					)
				);
			} else if (p.getName().equals(name[i])) {
				if (!p.isAvailable() && Integer.parseInt(amount[i]) > 0)
					p.notifyObservers(1);
				p.increaseAmount(Integer.parseInt(amount[i]));
				ret[0] = Boolean.toString(true);
			}
		}

		ret[1] = "";
		return ret;
	}

	static Market getInstance() {
		return instance;
	}
}
