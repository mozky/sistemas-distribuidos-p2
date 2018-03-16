package server;

import utils.CustomClock;

import java.net.*;
import java.io.*;


public class Server {
    public static void main(String[] args) {
        int portNumber = 6666;
        boolean listening = true;

        //Creating the main clock
        CustomClock clock = new CustomClock();

        //We start listening for socket connections
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                System.out.println("Server started... listening for connections");
                new ServerThread(serverSocket.accept(), clock).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
