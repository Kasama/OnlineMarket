package br.usp.icmc.onlinemarket;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SessionManager {

	static SessionManager instance = null;
	private ArrayList<String> sessionTokens;

	private SessionManager(){
		sessionTokens = new ArrayList<>();
	}

	public SessionManager getInstance(){
		if (instance == null)
			instance = new SessionManager();
		return instance;
	}

	public boolean logout(String token){

		return sessionTokens.remove(token);

	}

	public String login(String user, String password){

		String tomd5 = user+password;
		String token = "";

		MessageDigest md = null;
		BigInteger bi;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ignored){}

		assert md != null;
		bi = new BigInteger(1, md.digest(tomd5.getBytes()));

		if (DataManager.getInstance().verifyLogin(user, bi.toString(16))) {
			token = bi.toString(16);
			if (!sessionTokens.contains(token))
				sessionTokens.add(token);
		}

		return token;

	}


}
