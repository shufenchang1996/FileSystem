package com.example.filesystem;

import com.example.filesystem.Services.Database;
import com.example.filesystem.Services.FileSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class MainApplication extends Application{

    //init() the MainApplication instance, then execute start() after
    @Override
    public void init() throws Exception{

        //create a FileSystem with its assigned directory
        Path currentRelativePath = Paths.get("");
        FileSystem fs = new FileSystem(currentRelativePath.toAbsolutePath().toString());

        fs.readFiles(currentRelativePath.toAbsolutePath().toString()); //read all directories and files under requested root directory
        fs.createStructTxt(); //create a txt file called "fileStruct.txt" which lists all files and directories in the assigned directory

        Database db = new Database(fs.getDataListFromTxt("fileStruct.txt")); //get the dataList of the above fileStruct.txt file and get ready for db operation
        db.connectDb("connect", null); //connect to db, return a null arraylist<Data>

    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setTitle("File System Search");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}