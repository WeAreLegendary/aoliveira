package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Socket otherClientSocket;
    private String name;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket, Socket otherClientSocket, String name) {
        this.clientSocket = clientSocket;
        this.otherClientSocket = otherClientSocket;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(otherClientSocket.getOutputStream(), true);

            String msg;
            while ((msg = in.readLine()) != null) {
                out.println(name + ": " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

