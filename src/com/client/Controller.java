package com.client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

import com.socketfx.*;

public class Controller implements Initializable {

    private Timeline clockTicker = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> refreshClocks()));
    private static final int WAITING = 0;
    private static final int SECONDS = 1;
    private static final int MINUTES = 2;
    private static final int HOURS = 3;

    private boolean connected;
    private boolean configured;

    public int state = WAITING;

    @FXML
    private Label clockLabel;

    @FXML
    private ComboBox clockSelector;

    private FxSocketClient socket;

    public Controller() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeSelector(this.clockSelector);
        clockTicker.setCycleCount(Timeline.INDEFINITE);
        clockTicker.play();

        connected = false;

        this.connect();

        Runtime.getRuntime().addShutdownHook(new ShutDownThread());
    }

    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(),
                "localhost",
                6666,
                Constants.instance().DEBUG_NONE);
        socket.connect();
    }

    class ShutDownThread extends Thread {
        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    System.out.println("ShutdownHook: Shutting down Server Socket");
                }
                socket.shutdown();
            }
        }
    }

    class FxSocketListener implements SocketListener {

        @Override
        public void onMessage(String line) {
            if (line != null && !line.equals("") && configured) {
                clockLabel.setText(line);
                System.out.println("Recibí de master: " + line);
            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {
            if (isClosed) {
                connected = false;
            } else {
                connected = true;
            }
        }
    }

    @FXML
    private void refreshClocks() {
        if (state != WAITING) {
            changeClockLabels(state);
        }
    }

    private void initializeSelector(ComboBox selector) {
        selector.getItems().addAll("Segundos", "Minutos", "Horas");

        selector.setOnAction((event) -> {
            String selection = String.valueOf(selector.getSelectionModel().getSelectedItem());
            switch (selection) {
                case "Segundos":
                    this.state = 1;
                    break;
                case "Minutos":
                    this.state = 2;
                    break;
                case "Horas":
                    this.state = 3;
                    break;
                default:
                    this.state = 0;
            }
        });
    }

    private void changeClockLabels(int currentState) {
        switch (currentState) {
            case SECONDS:
                socket.sendMessage("seconds");
                configured = true;
                break;
            case MINUTES:
                socket.sendMessage("minutes");
                configured = true;
                break;
            case HOURS:
                socket.sendMessage("hours");
                configured = true;
                break;
            default:
                break;
        }
    }
}
