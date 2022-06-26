package com.example.filesystem.Controller;

import com.example.filesystem.Model.Data;
import com.example.filesystem.Services.Database;
import com.example.filesystem.Services.FileSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

//gui operation

public class Controller {

    @FXML
    private TextField searchBox;
    @FXML
    private TextArea resultBlock;

    private ArrayList<Data> result = new ArrayList<>();

    //action performed after the "search" button clicked
    @FXML
    protected void onButtonClick() {
        result.clear();
        String searchKeyword = searchBox.getText();

        try{
            //connect to database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/file","root","password");
            FileSystem fs = new FileSystem(Paths.get("").toAbsolutePath().toString());
            Database db = new Database(fs.getDataListFromTxt("fileStruct.txt")); //check db data

            //search within db
            String searchQuery = "SELECT name,path,type FROM node WHERE name LIKE \"%" + searchKeyword + "%\";";
            try {
                PreparedStatement stmt = conn.prepareStatement(searchQuery);
                ResultSet res = stmt.executeQuery();
                while(res.next()){
                    result.add(new Data(res.getString("name"), res.getString("path"), res.getString("type"))); //name, path, type -> only display path list to user interface
                }
                res.close();
                stmt.close();
            } catch (SQLException SQLe) {
                System.out.println(SQLe.getMessage());
            }
            conn.close(); //disconnect database after operation done

        } catch(SQLException SQLe){

            System.out.println("SQLException: " + SQLe.getMessage());
            System.out.println("SQLState: " + SQLe.getSQLState());
            System.out.println("VendorError: " + SQLe.getErrorCode());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        displaySearchResult();
    }


    //display search result to user interface
    protected void displaySearchResult(){

        StringBuilder output = new StringBuilder(); //output string to display in TextArea
        if(!result.isEmpty()){
            for(Data data: result){
                output.append(data.getPath()+"\n");
            }
        }else{
            output.append("No file/directory match found.");
        }

        resultBlock.setText(output.toString());
    }

}
