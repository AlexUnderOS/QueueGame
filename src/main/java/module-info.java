module com.alexosta.demo_ {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.alexosta.demo_004 to javafx.fxml;
    exports com.alexosta.demo_004;
}