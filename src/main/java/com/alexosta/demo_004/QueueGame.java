package com.alexosta.demo_004;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.media.Media;

import java.io.*;
import java.util.*;
public class QueueGame extends Application {
    private GameController controller;
    private String diaText;
    private int points = 0;

    private static final Map<String, MediaPlayer> mediaPlayers = new HashMap<>();
    private static final Map<String, AudioClip> audioClips = new HashMap<>();

    private int currentLevel = 1;

    private boolean trueAnsw;


    private void increaseLevel() {
        currentLevel++;
        controller.setLevel(currentLevel);
    }

    public void setGameController(GameController controller) {
        this.controller = controller;
    }
    PriorityQueue<ClassStudents> students = new PriorityQueue<>(Comparator.comparing(ClassStudents::getName));
    @Override
    public void start(Stage stage) throws IOException {
        playAudio("audio/backMusic.wav", true, false, 0.4);
        FXMLLoader fxmlLoader = new FXMLLoader(QueueGame.class.getResource("game-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 350);
        stage.setTitle("Queue!");
        stage.setScene(scene);
        stage.show();

        GameController controller = fxmlLoader.getController();
        setGameController(controller);
        fillQueueWithRandomStudents();

        nextStudentGo(students, controller);
        controller.setDefaultStudentImg("imgs/people/defaultStudent.png");
        controller.getTrueAnswBtn().setOnMouseClicked(event -> {
            boolean userAnswer = true;
            checkAnswerAndHandle(userAnswer, controller);
        });

        controller.getFalseAnswBtn().setOnMouseClicked(event -> {
            boolean userAnswer = false;
            checkAnswerAndHandle(userAnswer, controller);
        });
    }

    private void checkAnswerAndHandle(boolean userAnsw, GameController controller) {
        ClassStudents currentStudent = getCurrentStudent();
        if (currentStudent != null) {
            boolean isCorrect = checkAnswer(userAnsw);
            handleAnswer(isCorrect, controller);
        }

        if (students.isEmpty()) {
            increaseLevel();
            try {
                fillQueueWithRandomStudents();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    private void fillQueueWithRandomStudents() throws IOException {
        int maxStudents = 10 + (currentLevel - 1) * 5;

        for (int i = 0; i < maxStudents; i++) {
            String studentName = getRandomLineFromDoc(readLinesFromResource("test_sorting.txt"));
            ClassStudents student = new ClassStudents(studentName);
            students.offer(student);
        }
    }

    private boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }
    private ClassStudents getCurrentStudent() {
        return students.peek();
    }

    private void handleAnswer(boolean isCorrect, GameController controller) {
        if (isCorrect) {
            playAudio("audio/happyPerson.wav", false, true, 0.2);
            controller.setStudentImgForSec("imgs/people/happyStudent.png", 1);
            this.points++;
            controller.setPoint(points);
        } else {
            playAudio("audio/sadPerson.wav", false, true, 0.2);
            controller.setStudentImgForSec("imgs/people/sadStudent.png", 1);
        }

        System.out.println("Points: " + this.points);

        if (!students.isEmpty()) {
            try {
                nextStudentGo(students, controller);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

    private enum Operation {
        ADDITION("+"),
        SUBTRACTION("-"),
        MULTIPLICATION("*"),
        DIVISION("/");

        private final String symbol;

        Operation(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
    public String randomMathExample() {
        Random rand = new Random();
        int a = rand.nextInt(20) + 1;
        int b = rand.nextInt(20) + 1;

        Operation operation = Operation.values()[rand.nextInt(Operation.values().length)];

        int tmpResult = 0;
        double tmpDivisionResult = 0;
        String finalResult = "";
        boolean haveDivision = false;

        switch (operation) {
            case ADDITION:
                tmpResult = a + b;
                break;
            case SUBTRACTION:
                if (a < b) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                tmpResult = a - b;
                break;
            case MULTIPLICATION:
                tmpResult = a * b;
                break;
            case DIVISION:
                if (a < b){
                    int tmp = b;
                    b = a;
                    a = tmp;
                }

                if ((a % b) > 0){
                    tmpDivisionResult = (double) a / b;
                    haveDivision = true;
                }else{
                    tmpResult = a / b;
                }

                break;
        }

        trueAnsw = getRandomBoolean();
        if (!trueAnsw){
            if (!haveDivision){
                tmpResult = rand.nextInt(Math.max(1, tmpResult)) + 5;
                finalResult = Integer.toString(tmpResult);
            }else {
                tmpDivisionResult = rand.nextInt(1) + 5;
                finalResult = Double.toString(tmpDivisionResult);
            }
        }else{
            if (!haveDivision){
                finalResult = Integer.toString(tmpResult);
            }else {
                finalResult = Double.toString(tmpDivisionResult);
            }
        }



        return String.format("%d %s %d = %s", a, operation.getSymbol(), b, finalResult);
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

    public boolean checkAnswer(boolean userAnswer) {
        return userAnswer == trueAnsw;
    }


    public void nextStudentGo(PriorityQueue<ClassStudents> students, GameController controller) throws IOException {
        diaText = getRandomLineFromDoc(readLinesFromResource("Dialogues.txt"));
        ClassStudents nextStudent = students.poll();
        if (nextStudent != null) {
            String mathExample = randomMathExample();
            controller.setStudentName(nextStudent.getName());
            controller.setMathExampleText(mathExample);
            controller.setDiaText(diaText);
            controller.setStudents_hBox(students);
        }
    }
}