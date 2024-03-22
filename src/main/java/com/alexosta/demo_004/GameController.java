package com.alexosta.demo_004;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
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

    @FXML
    private Label rezultLabel;
    @FXML
    private Label maxRezultLabel;
    @FXML
    private ImageView backImageView;

    private final String SCORES_FILE_PATH = "com/alexosta/demo_004/Scores.txt";



    Image defaultStudentImg;

    void handleTimerFinish() {
        timer.stopTimer();
        disableButtons();

//        int currentPoints = Integer.parseInt(pointsLabel.getText().split(": ")[1].trim());

//        updateMaxScore(currentPoints);

//        rezultLabel.setText("Current Points: " + currentPoints);
//        maxRezultLabel.setText("Max Score: " + getMaxScore());
    }

    private void updateMaxScore(int currentPoints) {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE_PATH))) {
            String line;
            int maxScore = 0;

            while ((line = reader.readLine()) != null) {
                int score = Integer.parseInt(line.trim());
                if (score > maxScore) {
                    maxScore = score;
                }
            }

            if (currentPoints > maxScore) {
                try (FileWriter writer = new FileWriter("Scores.txt")) {
                    writer.write(Integer.toString(currentPoints));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private int getMaxScore() {
//        try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE_PATH))) {
//            String line;
//            int maxScore = 0;
//
//            while ((line = reader.readLine()) != null) {
//                int score = Integer.parseInt(line.trim());
//                if (score > maxScore) {
//                    maxScore = score;
//                }
//            }
//            return maxScore;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }

    public void setStudentImgForSec(String imgName, double timeAmount) {
        studentImgView.setImage(new Image(getClass().getResourceAsStream(imgName)));
        PauseTransition pause = new PauseTransition(Duration.seconds(timeAmount));

        pause.setOnFinished(e -> {
            slowFade(studentImgView, 0.4);
            studentImgView.setImage(defaultStudentImg);
        });

        pause.play();
    }

    private CountdownTimer timer;

    public void setTimerTime(String time) {
        timerLabel.setText(time);
    }

    public void disableButtons() {
        trueAnswBtn.setDisable(true);
        falseAnswBtn.setDisable(true);
    }


    public void setDefaultStudentImg(String imgName){
        defaultStudentImg = new Image(getClass().getResourceAsStream(imgName));
        studentImgView.setImage(defaultStudentImg);
    }

    public void setStudentValue(int value) {
        studentValueLabel.setText("Rinda no "+value+" st. ");
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

    @FXML
    private void initialize() {
        QueueGame queueGame = new QueueGame();
        queueGame.setGameController(this);
        timer = new CountdownTimer(this);
        timer.startTimer();
    }
    public void setLevel(int level) {
        lvlLabel.setText("LVL: " + level);
    }

    public void setPoint(int points) {
        pointsLabel.setText("Punkti: " + points);
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
