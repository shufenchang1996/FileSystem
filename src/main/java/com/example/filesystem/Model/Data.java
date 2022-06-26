package com.example.filesystem.Model;

/**
 * A row of data in the database which records a file/directory read in the root directory
 */
public class Data {

    //should i have an id variable for my file/folder??? how to set id?
    private int id;
    private String name;
    private String path;
    private String type; //file, directory

    public Data(String fileName, String filePath, String fileType){
        this.name = fileName;
        this.path = filePath;
        this.type = fileType;
    }

    public void setID(int id){ this.id = id; }
    public int getId(){ return this.id; }
    public String getName(){ return this.name; }
    public String getPath(){ return this.path; }
    public String getType(){ return this.type; }

}

