package com.alexosta.demo_004;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Iterator;
import java.util.PriorityQueue;

public class GameController {
    @FXML
    private Label studentNameLabel = new Label();

    @FXML
    private Label mathExampleLabel = new Label();

    @FXML
    private Label studentValueLabel = new Label();

    @FXML
    private Label lvlLabel = new Label();

    @FXML
    private Label timerLabel = new Label();

    @FXML
    private Label diaLabel = new Label();

    @FXML
    private ImageView studentImgView;

    @FXML
    private HBox students_hBox;

    @FXML
    private Button trueAnswBtn, falseAnswBtn;


    public void setStudentImg(String imgName) {
        studentImgView.setImage(new Image(
                getClass().getResourceAsStream(imgName)));
    }

    public ImageView getStudentImgView() {
        return studentImgView;
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
    public void setStudents_hBox(PriorityQueue<String> students){
        students_hBox.getChildren().clear();
        Iterator<String> iterator = students.iterator();
        while (iterator.hasNext()) {
            String studentName = iterator.next();
            students_hBox.getChildren().add(new Label(studentName));
        }
    }
}
