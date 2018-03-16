package server;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import utils.CustomClock;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller {

    private Timeline clockTicker = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> changeClockLabels()));

    @FXML
    private Label clockLabel;

    @FXML
    private Slider clockSlider;

    private CustomClock clock = new CustomClock();

    public Controller() {
    }

    @FXML
    private void initialize() {
        clockSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Slider Value Changed: " + newValue.doubleValue() + ")");
            clock.setTickSpeed(newValue.doubleValue());
        });

        clockTicker.setCycleCount(Timeline.INDEFINITE);
        clockTicker.play();
    }

    @FXML
    private void refreshClocks() {
        changeClockLabels();
    }

    //TODO: Change these functions for a switch with the id of the button
    @FXML
    private void handlePlusHour() {
        this.clock.plusHour();
        changeClockLabels();
    }

    @FXML
    private void handleMinusHour() {
        this.clock.minusHour();
        changeClockLabels();
    }

    @FXML
    private void handlePlusMinute() {
        this.clock.plusMinute();
        changeClockLabels();
    }

    @FXML
    private void handleMinusMinute() {
        this.clock.minusMinute();
    }

    private void changeClockLabels() {
        clockLabel.setText(this.clock.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
