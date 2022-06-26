module com.example.filesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.filesystem to javafx.fxml;
    exports com.example.filesystem;
    opens com.example.filesystem.Controller to javafx.fxml; //get controller explicitly
    exports com.example.filesystem.Controller; //get controller explicitly
}