package com.alexosta.demo_004;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Iterator;
import java.util.Objects;
import java.util.PriorityQueue;

public class GameController {
    @FXML
    private Label studentNameLabel;

    @FXML
    private Label mathExampleLabel;

    @FXML
    private Label studentValueLabel;

    @FXML
    private Label lvlLabel;

    @FXML
    private Label timerLabel = new Label();

    @FXML
    private Label diaLabel;

    @FXML
    private Label pointsLabel;

    @FXML
    private ImageView studentImgView;

    @FXML
    private HBox students_hBox;

    @FXML
    private Button trueAnswBtn, falseAnswBtn;

    private CountdownTimer timer;
    @FXML
    private void initialize() {
        QueueGame queueGame = new QueueGame();
        queueGame.setGameController(this);
        timer = new CountdownTimer(this);
        timer.startTimer();
    }
    public void setTimerTime(String time) {
        timerLabel.setText(time);
    }
    public void handleTimerFinish() {
        timer.stopTimer();
        disableButtons();
    }
    public void disableButtons() {
        trueAnswBtn.setDisable(true);
        falseAnswBtn.setDisable(true);
    }

    private Image defaultStudentImg;
    public void setStudentImgForSec(String imgName, double timeAmount) {
        studentImgView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imgName))));
        PauseTransition pause = new PauseTransition(Duration.seconds(timeAmount));

        pause.setOnFinished(e -> {
            slowFade(studentImgView, 0.4);
            studentImgView.setImage(defaultStudentImg);
        });

        pause.play();
    }
    public void setDefaultStudentImg(String imgName){
        defaultStudentImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imgName)));
        studentImgView.setImage(defaultStudentImg);
    }
    public void setStudentValue(int value) {
        studentValueLabel.setText("Rinda no "+value+" st. ");
    }

    public void setLevel(int level) {
        lvlLabel.setText("LVL: " + level);
    }

    public void setPoint(int points) {
        pointsLabel.setText("Punkti: " + points);
    }

    public void playEnterAnimationBtnTrue() {
        animateUsingScaleTransition(getTrueAnswBtn(), 0.1, true);
    }
    public void playExitAnimationBtnTrue() {
        animateUsingScaleTransition(getTrueAnswBtn(), 0.2, false);
    }

    public void playEnterAnimationBtnFalse() {
        animateUsingScaleTransition(getFalseAnswBtn(), 0.1, true);
    }
    public void playExitAnimationBtnFalse() {
        animateUsingScaleTransition(getFalseAnswBtn(), 0.2, false);
    }


    public void setStudentName(String name){
        studentNameLabel.setText(name);
    }
    public void setDiaText(String text){
        diaLabel.setText(text);
    }
    public void setMathExampleText(String text){
        mathExampleLabel.setText(text);
    }
    public Button getTrueAnswBtn() {
        return trueAnswBtn;
    }
    public Button getFalseAnswBtn() {
        return falseAnswBtn;
    }
    public void setStudents_hBox(PriorityQueue<ClassStudents> students){
        students_hBox.getChildren().clear();
        Iterator<ClassStudents> iterator = students.iterator();
        while (iterator.hasNext()) {
            ClassStudents student = iterator.next();
            Label st = new Label(student.getName());
            st.setTextFill(Color.WHITE);
            students_hBox.getChildren().add(st);
        }
        setStudentValue(students.size());
    }

    private void animateUsingScaleTransition(Button obj, double speed, boolean isEntered) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(speed), obj);
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(speed), obj);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        if (isEntered) {
           scaleUp.play();
        }else {
            scaleDown.play();
        }
    }

    public void slowFade(ImageView view, double speedSec) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(view);
        fadeTransition.setDuration(Duration.seconds(speedSec));

        if (fadeTransition.getByValue() == 1.0){
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
        } else if (fadeTransition.getByValue() == 0.0){
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
        }

        fadeTransition.play();
    }
}
