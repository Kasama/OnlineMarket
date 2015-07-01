package br.usp.icmc.onlinemarket;

import java.util.Arrays;
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
		){
			ret[0] = Boolean.toString(true);
			ret[1] = "";
		}else {
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
		switch (mode){
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

		ret = new String[products.size()*7];

		int i = 0;

		for (Product p : products){
			ret[i++] = String.valueOf(p.getId());
			ret[i++] = p.getName();
			ret[i++] = String.valueOf(p.getPrice());
			ret[i++] = p.getBestBefore();
			ret[i++] = String.valueOf(p.getProvider());
			ret[i++] = p.isAvailable() ? "Available":"Unavailable";
			ret[i++] = String.valueOf(p.getAmount());
		}

		return ret;
	}

	public String[] subscribe(String token, String productId) {
		User user = sessionManager.getUserByToken(token);
        Product product = dataManager.getProductById(Integer.parseInt(productId));
        String ret[] = new String[1];

        if(user.isCustomer() && !product.isAvailable()){
            product.addObserver(user);
            ret[0] = Boolean.toString(true);
        }
        else {
            ret[0] = Boolean.toString(false);
        }

		return ret;
	}

	public String[] buyProduct(
		String token, String[] productId, String[] amount
	) {
		String[] ret;

		User u = sessionManager.getUserByToken(token);
		if (!u.isCustomer()){
			ret = new String [productId.length+1];
			ret[0] = Boolean.toString(false);
			System.arraycopy(productId, 0, ret, 1, productId.length);
			return ret;
		}
		for (String idS : productId){
			long id = Long.parseLong(idS);
		}

		return ret;
	}

	public String[] addProduct(
		String token, String[] id, String[] name, String[] price, String[]
		bestBefore, String[] amount
	) {
		return new String[0];
	}

	static Market getInstance() {
		return instance;
	}
}
