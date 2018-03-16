package com.server;

import com.utils.CustomClock;

import java.net.*;
import java.io.*;


public class ServerThread extends Thread {
    private Socket socket;
    private CustomClock clock;

    public ServerThread(Socket socket, CustomClock clock) {
        super("ClockThread");
        this.socket = socket;
        this.clock = clock;
    }

    public void run() {

        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        ) {
            String inputLine, outputLine;
            ClockProtocol cp = new ClockProtocol(this.clock);
            outputLine = cp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = cp.processInput(inputLine);
                out.println(outputLine);

                if (outputLine.equals("Bye"))
                    break;
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
