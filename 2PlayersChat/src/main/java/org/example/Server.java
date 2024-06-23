package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Server started...");

            while (true) {
                Socket clientSocket1 = serverSocket.accept();
                BufferedReader in1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
                String name1 = in1.readLine();
                System.out.println(name1 + " connected");

                Socket clientSocket2 = serverSocket.accept();
                BufferedReader in2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
                String name2 = in2.readLine();
                System.out.println(name2 + " connected");

                Thread handler1 = new Thread(new ClientHandler(clientSocket1, clientSocket2, name1));
                Thread handler2 = new Thread(new ClientHandler(clientSocket2, clientSocket1, name2));
                handler1.start();
                handler2.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
