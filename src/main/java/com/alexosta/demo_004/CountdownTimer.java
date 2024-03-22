package com.alexosta.demo_004;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class CountdownTimer {
    private int secondsRemaining = 60;
    private Timeline timeline;
    private final GameController controller;

    public CountdownTimer(GameController controller) {
        this.controller = controller;
    }

    public void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            secondsRemaining--;
            updateTimerLabel();
            if (secondsRemaining <= 0) {
                timeline.stop();
                // game block
                controller.disableButtons();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void resetTimer() {
        secondsRemaining = 60;
        updateTimerLabel();
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
            controller.handleTimerFinish();
        }
    }

    private void updateTimerLabel() {
        int seconds = secondsRemaining % 60;

        String timeString = String.format("%02d", seconds);
        controller.setTimerTime(timeString);
    }


}
