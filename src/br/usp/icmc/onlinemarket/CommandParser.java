package br.usp.icmc.onlinemarket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CommandParser implements Runnable {
    Scanner in;
    PrintWriter out;

    public CommandParser(Socket s) {
        try {
            in = new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        String s;
        String[] tokens;

        while (in.hasNext()) {
            s = in.nextLine();
            if(s.startsWith("<") && s.endsWith(">")) {
                tokens = s.split("|");
                handleCommand(tokens);
            }
        }

    }

    private void handleCommand(String[] tokens) {

        switch (tokens[0]){
            case "add":
                handleAdd(tokens);
                break;
            case "newuser":
                handleNewUser(tokens);
                break;
            case "login":
                handleLogin(tokens);
                break;
            case "request":
                handleRequest(tokens);
                break;
            case "subscribe":
                handleSubscribe(tokens);
                break;
            case "buy":
                handleBuy(tokens);
                break;
            case "addProduct":
                handleAddProduct(tokens);
                break;
        }

    }

    private void handleAddProduct(String[] tokens) {
        final int TOKEN = 1;
        final int ID = 2;
        final int NAME = 3;
        final int PRICE = 4;
        final int BESTBEFORE = 5;
        final int AMOUNT = 6;
        final int nItems = (tokens.length - 2)/5;



    }

    private void handleBuy(String[] tokens) {

    }

    private void handleSubscribe(String[] tokens) {

    }

    private void handleRequest(String[] tokens) {

    }

    private void handleNewUser(String[] tokens) {

    }

    private void handleLogin(String[] tokens) {

    }

    private void handleAdd(String[] tokens) {

    }
}
