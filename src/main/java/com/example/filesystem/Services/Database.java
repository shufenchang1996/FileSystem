package com.example.filesystem.Services;


import com.example.filesystem.Model.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Database related operations:
 * 1. connect to database
 * 2. initialise database when connected
 * 3. insert data into database upon requested
 * 4. search for certain data upon requested
 * */

public class Database {

    private Connection conn;
    private static final String username = "root";
    private static final String password = "password";
    private static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String dbURL = "jdbc:mysql://localhost:3306/file";
    private ArrayList<Data> dataList = new ArrayList<>(); //a list of file/directory data
    private ArrayList<Data> result = new ArrayList<>();

    public Database(ArrayList<Data> dataListInput){
        this.dataList = dataListInput;
    }

    //connect to database
    //operation keyword is "connect"
    //searchKeyword: "a keyword", or null
    public ArrayList<Data> connectDb(String operation, String searchKeyword){
        result.clear();

        if (conn == null) {
            try {
                Class.forName(dbDriver);
                conn = DriverManager.getConnection(dbURL, username, password);

                initDb();
                insertDb();

                conn.close(); //disconnect database after operation done
                conn = null;
            } catch (SQLException SQLe) {
                System.out.println("SQLException: " + SQLe.getMessage());
                System.out.println("SQLState: " + SQLe.getSQLState());
                System.out.println("VendorError: " + SQLe.getErrorCode());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    //initialise database when connected
    private void initDb(){
        //ensure a clean database when initialised
        String cleanDb = "DROP TABLE IF EXISTS node;";

        //create a table "node" to store all the directories and files
        String createDb = "CREATE TABLE node " +
                "(id int PRIMARY KEY," +
                "  name varchar(100) NOT NULL," +
                "  path varchar(6000) NOT NULL," +
                "  type ENUM ('Directory', 'File')" +
                ");";

        try {
            PreparedStatement stmt = conn.prepareStatement(cleanDb);
            stmt.execute();
            stmt = conn.prepareStatement(createDb);
            stmt.execute();
        } catch (SQLException SQLe) {
            System.out.println(SQLe.getMessage());
        }
    }

    //insert data into database upon requested
    private void insertDb(){
        String insertQuery = "INSERT INTO node(id,name,path,type) " + "VALUES(?,?,?,?);";
        try {
            PreparedStatement stmt = conn.prepareStatement(insertQuery);

            int id = 0; //current id of this batch of sql queries to insert
            for (Data values : dataList) {

                /**WRITE SET ID for each data**/
                stmt.setInt(1, values.getId());
                stmt.setString(2, values.getName());
                stmt.setString(3, values.getPath());
                stmt.setString(4, values.getType());
                stmt.addBatch();
                id++;

                // execute query batch when reaching 1000 queries
                // or total queries amount equals to dataList.size()
                if (id == dataList.size() || id % 1000 == 0) {
                    stmt.executeBatch();
                }
            }
            stmt.close();
        } catch (SQLException SQLe) {
            System.out.println(SQLe.getMessage());
        }

    }

}
