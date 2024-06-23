package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Player2 {

    public static void main(String[] args) {
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);
        final String[] fullMessage = new String[1];
        final String name;

        final Map<String, Integer> messagesCounter = new HashMap<>();
        messagesCounter.put("MessagesSend", 0);
        messagesCounter.put("MessagesReceived", 0);

        try {
            clientSocket = new Socket("127.0.0.1", 5000);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.print("Enter your name: ");
            name = sc.nextLine();
            out.println(name);

            Thread sender = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    boolean flag = true;
                    while (flag) {

                        int msgRecCount = messagesCounter.getOrDefault("MessagesReceived", 0);
                        int msgSentCount = messagesCounter.getOrDefault("MessagesSend", 0);

                        if (msgSentCount - msgRecCount <= 0) {
                            msg = sc.nextLine();
                            if (msg != null && !msg.isBlank()) {
                                out.println(fullMessage[0] != null ? fullMessage[0] + msg + " " : msg + " ");
                                messagesCounter.put("MessagesSend", messagesCounter.get("MessagesSend") + 1);
                            }
                        }

                        if (messagesCounter.get("MessagesSend") == 10 && messagesCounter.get("MessagesReceived") == 10) {
                            flag = false;
                        }

                    }
                }
            });
            sender.start();

            Thread receiver = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        while ((msg = in.readLine()) != null) {

                            System.out.println(msg);
                            fullMessage[0] = msg.substring(msg.indexOf(":") + 2);
                            messagesCounter.put("MessagesReceived", messagesCounter.get("MessagesReceived") + 1);

                            if (messagesCounter.get("MessagesSend") == 10 && messagesCounter.get("MessagesReceived") == 10) {
                                System.out.println("Game over! The full phrase is: " + fullMessage[0]);
                                break;
                            }
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
            });
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
