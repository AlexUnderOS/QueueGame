package com.alexosta.demo_004;

import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;

import java.io.*;
import java.util.*;

public class QueueGame extends Application {
    private String studentName;
    private String diaText;
    private int lvl = 1;
    private boolean currentAnswIsTrue;
    private int points = 0;

    private static final Map<String, MediaPlayer> mediaPlayers = new HashMap<>();
    private static final Map<String, AudioClip> audioClips = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException {
        playAudio("audio/backMusic.wav", true, false, 0.3);

        FXMLLoader fxmlLoader = new FXMLLoader(QueueGame.class.getResource("game-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 350);
        stage.setTitle("Queue!");
        stage.setScene(scene);
        stage.show();

        PriorityQueue<String> students = new PriorityQueue<>();

        GameController controller = fxmlLoader.getController();

        for (int i = 0; i < 10; i++){
            studentName = getRandomLineFromDoc(readLinesFromResource("Students.txt"));
            students.add(studentName);
        }
        System.out.println(students);

        nextStudentGo(students, controller);

        controller.getTrueAnswBtn().setOnAction(event -> {
            if (!students.isEmpty()){
                animateUsingScaleTransition(controller.getTrueAnswBtn(), 0.2, false);
                try {
                    nextStudentGo(students, controller);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                playAudio("audio/happyPerson.wav", false, true, 0.2);
                System.out.println(students);
                checkUserAnswer(true);
            }
        });
        controller.getFalseAnswBtn().setOnAction(event -> {
            if (!students.isEmpty()){
                animateUsingScaleTransition(controller.getFalseAnswBtn(), 0.2, false);
                try {
                    nextStudentGo(students, controller);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                playAudio("audio/sadPerson.wav", false, true, 0.2);
                checkUserAnswer(false);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

    private List<String> readLinesFromResource(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private String getRandomLineFromDoc(List<String> lines) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(lines.size());
        return lines.get(randomIndex);
    }

    public String randomMathExample(){
        int lvlUp = lvl * 2;
        Random rand = new Random();
        String[] actions = {"+", "-", "*", "/"};
        int a = rand.nextInt(lvlUp+1)+1;
        int b = rand.nextInt(lvlUp+1)+1;
        int randAction = rand.nextInt(actions.length);
        String action = actions[randAction];
        double answ = 0;

        switch (action){
            case "+":
                answ = a + b;
                break;
            case "-":
                answ = a - b;
                break;
            case "*":
                answ = a * b;
                break;
            case "/":
                if (a < b){
                    int temp = a;
                    a = b;
                    b = temp;
                }
                answ = (double) a / b;
                break;
        }

        boolean answIsTrue = getRandomBoolean();
        setCurrentAnswerIs(answIsTrue);

        if (answIsTrue) {
            System.out.println(1);
            if (answ == (int)answ) {
                int intAnswer = (int)answ;
                return (a+" "+action+" "+b+" = "+intAnswer);
            }else {
                return (a+" "+action+" "+b+" ~ "+answ);
            }
        }else {
            System.out.println(2);

            if (answ == (int)answ) {
                int intAnswer = rand.nextInt((int) answ+5);
                return (a+" "+action+" "+b+" = "+intAnswer);
            }else {
                answ = rand.nextDouble(answ)+5;
                return (a+" "+action+" "+b+" ~ "+answ);
            }
        }

    }

    public boolean getRandomBoolean() {
        Random rand = new Random();
        return rand.nextBoolean();
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

    public void playAudio(String fileName, boolean isLooped, boolean isShortSound, double volume) {
        if (isShortSound) {
            AudioClip audioClip = new AudioClip(getClass().getResource(fileName).toExternalForm());
            audioClip.play();
            audioClips.put(fileName, audioClip);
            audioClip.setVolume(volume);
        } else {
            String path = getClass().getResource(fileName).getPath();
            Media media = new Media(new File(path).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(isLooped ? MediaPlayer.INDEFINITE : 1);
            mediaPlayer.play();
            mediaPlayers.put(fileName, mediaPlayer);
            mediaPlayer.setVolume(volume);
        }
    }

    public void checkUserAnswer(boolean userAnswIsTrue) {
        if (userAnswIsTrue == currentAnswIsTrue){
            System.out.println("yes");
            points++;
        }else {
            System.out.println("no");
        }
    }

    public void setCurrentAnswerIs(boolean currentAnswerIsTrue){
        this.currentAnswIsTrue = currentAnswerIsTrue;
    }

    public void nextStudentGo(PriorityQueue<String> students, GameController controller) throws IOException {
        diaText = getRandomLineFromDoc(readLinesFromResource("Dialogues.txt"));
        controller.setStudentName(students.peek());
        controller.setDiaText(diaText);
        controller.setStudentImg("imgs/people/defaultPeople.png");
        controller.setMathExampleText(randomMathExample());
        slowFade(controller.getStudentImgView(), 0.9);
        controller.setStudents_hBox(students);
        students.poll();
    }

    private void animateUsingScaleTransition(Object heart, double speed, boolean isLooped) {
        ScaleTransition scaleTransition = new ScaleTransition(
                Duration.seconds(speed), (Node) heart
        );
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setFromZ(1);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setToZ(1.1);
        scaleTransition.setAutoReverse(true);
        if (isLooped){
            scaleTransition.setCycleCount(Animation.INDEFINITE);
        } else {
            scaleTransition.setCycleCount(2);
        }

        RotateTransition rotateTransition = new RotateTransition(
                Duration.seconds(speed), (Node) heart
        );

        rotateTransition.setByAngle(10);


        rotateTransition.setAutoReverse(true);
        if (isLooped){
            rotateTransition.setCycleCount(Animation.INDEFINITE);
        } else {
            rotateTransition.setCycleCount(2);
        }

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(scaleTransition, rotateTransition);
        parallelTransition.play();
    }
}