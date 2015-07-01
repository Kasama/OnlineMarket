package br.usp.icmc.onlinemarket;

import com.sun.org.apache.xpath.internal.operations.Bool;
import jdk.nashorn.internal.objects.NativeUint16Array;

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
        Product product = dataManager.getProductById(Long.parseLong(productId));
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
		int i = 0;

		for (String id : productId){
			Product product = dataManager.getProductById(Long.parseLong(id));
			if (!product.isAvailable()) {
				//TODO buy
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

        if(!u.isProvider()) {
            ret[0] = Boolean.toString(false);
            ret[1] = "Error, permission denied.";
            return ret;
        }

        Product p = dataManager.getProductById(Integer.parseInt(id[0]));
        if(p == null){
            ret[0] = Boolean.toString(
                    dataManager.addProduct(Long.parseLong(id[0]),
                            name[0],Double.parseDouble(price[0]),
                            bestBefore[0], Long.parseLong(amount[0]),
                            u.getId()
                    ));
        }else if(p.getName().equals(name[0])){
            p.increaseAmount(Integer.parseInt(amount[0]));
            ret[0] = Boolean.toString(true);
        }

        ret[1] = "";
        return ret;
	}

	static Market getInstance() {
		return instance;
	}
}
