package br.usp.icmc.onlinemarket;

public interface Market {

	String[] signUp(
		String username, String name, String address, long telephone,
		String email, long id, String password, String type
	);

	String[] login(
		String username, String passwordMd5
	);

	String[] request(String mode);

	String[] subscribe(String token, String productId);

	String[] buyProduct(
		String token, String[] productId, String[] amount
	);

	String[] addProduct(
		String token, String[] id, String[] name, String[] price, String[]
		bestBefore, String[] amount
	);

	static Market getInstance(){
		return new Market() {
			@Override
			public String[] signUp(
				String username, String name, String address, long telephone,
				String email, long id, String password, String type
			) {
				return new String[0];
			}

			@Override
			public String[] login(String username, String passwordMd5) {
				return new String[0];
			}

			@Override
			public String[] request(String mode) {
				return new String[0];
			}

			@Override
			public String[] subscribe(String token, String productId) {
				return new String[0];
			}

			@Override
			public String[] buyProduct(
				String token, String[] productId, String[] amount
			) {
				return new String[0];
			}

			@Override
			public String[] addProduct(
				String token, String[] id, String[] name, String[] price,
				String[] bestBefore, String[] amount
			) {
				return new String[0];
			}
		};
	}
}
