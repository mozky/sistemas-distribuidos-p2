package com.server;

import com.socketfx.Constants;
import com.socketfx.FxSocketClient;
import com.socketfx.SocketListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import com.utils.CustomClock;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private boolean connected;
    private boolean configured;

    @FXML
    private Label clockLabel;

    @FXML
    private Slider clockSlider;

    private CustomClock clock = new CustomClock();
    private FxSocketClient socket;

    public Controller() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clockSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Slider Value Changed: " + newValue.doubleValue() + ")");
            clock.setTickSpeed(newValue.doubleValue());
        });

        connected = false;

        this.connect();
    }

    class FxSocketListener implements SocketListener {

        @Override
        public void onMessage(String line) {
            if (line != null && !line.equals("")) {
                System.out.println("Server: " + line);
                if (!configured) {
                    socket.sendMessage("master");
                    configured = true;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    socket.sendMessage("");
                    clockLabel.setText(line);
                }
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

    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(),
                "localhost",
                6666,
                Constants.instance().DEBUG_NONE);
        socket.connect();
    }

    @FXML
    private void handlePlusHour() {
        socket.sendMessage("plusHour");
    }

    @FXML
    private void handleMinusHour() {
        socket.sendMessage("minusHour");
    }

    @FXML
    private void handlePlusMinute() {
        socket.sendMessage("plusMinute");
    }

    @FXML
    private void handleMinusMinute() {
        socket.sendMessage("minusMinute");
    }

}
