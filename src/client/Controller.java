package client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.Duration;
import utils.CustomClock;

public class Controller {

    private Timeline clockTicker = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> refreshClocks()));
    private static final int WAITING = 0;
    private static final int SECONDS = 1;
    private static final int MINUTES = 2;
    private static final int HOURS = 3;

    public int state = WAITING;

    @FXML
    private Label clockLabel;

    @FXML
    private ComboBox clockSelector;

    private CustomClock clock = new CustomClock();

    public Controller() {
    }

    @FXML
    private void initialize() {
        initializeSelector(this.clockSelector);
        clockTicker.setCycleCount(Timeline.INDEFINITE);
        clockTicker.play();
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
                clockLabel.setText(String.valueOf(this.clock.getTime().getSecond()));
                break;
            case MINUTES:
                clockLabel.setText(String.valueOf(this.clock.getTime().getMinute()));
                break;
            case HOURS:
                clockLabel.setText(String.valueOf(this.clock.getTime().getHour()));
                break;
            default:
                break;
        }
    }
}
