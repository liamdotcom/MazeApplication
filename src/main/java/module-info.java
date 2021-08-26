module com.MazeApplicationMaven {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.MazeApplicationMaven to javafx.fxml;
    exports com.MazeApplicationMaven;
}