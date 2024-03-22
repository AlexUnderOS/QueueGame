package com.alexosta.demo_004;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WinnerController {
    @FXML
    Button nextButton;
    @FXML
    public void switchStageToGame() throws IOException {
        // Получаем ссылку на текущую сцену и окно
        Stage stage = (Stage) nextButton.getScene().getWindow();

        // Загружаем новый FXML файл
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-scene.fxml"));
        Parent root = fxmlLoader.load();

        // Получаем ссылку на контроллер
        GameController controller = fxmlLoader.getController();

        Scene scene = new Scene(root);

        // Устанавливаем новую сцену для текущего окна
        stage.setScene(scene);

        // Показываем окно с новой сценой
        stage.show();
    }
}
