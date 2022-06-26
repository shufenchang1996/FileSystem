package com.example.filesystem.Services;

import com.example.filesystem.Model.Data;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileSystem {

    private static String rootDir = ""; //root directory path to read all files and directories from
    private ArrayList<Data> dataList = new ArrayList<>(); //for the sake of writing result to the txt

    public FileSystem(String rootPath){
        this.rootDir = rootPath;
    }

    //read all directories and files under requested root directory
    public void readFiles(String dirPath){
        File dir = new File(dirPath);

        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    dataList.add(new Data(file.getName(), file.getAbsolutePath(), "Directory"));
                    readFiles(file.getAbsolutePath());
                } else {
                    dataList.add(new Data(file.getName(), file.getAbsolutePath(), "File"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create a new txt file
    // write the file system structure to a txt (FileSystem.txt)
    public void createStructTxt(){
        try {
            File struct = new File(rootDir + File.separator + "fileStruct.txt");

            struct.createNewFile();
            StringBuilder structStr = new StringBuilder();

            for (Data data : dataList) {
                structStr.append(data.getPath());
                structStr.append("\t");
                structStr.append(data.getType());
                structStr.append("\n");
            }

            FileWriter fileWrite = new FileWriter(struct, false);
            fileWrite.write(structStr.toString());
            fileWrite.flush();
            fileWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // from txt to ArrayList<Data> variable: result
    public ArrayList<Data> getDataListFromTxt(String txtPath){
        ArrayList<Data> result = new ArrayList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(txtPath));
            String line = reader.readLine(), path = "", type = "", name = "";
            int id = 1; //data id starts from 1
            Data data;
            while (line != null) {
                path = line.split("\t")[0];
                type = line.split("\t")[1];
                name = path.substring(path.lastIndexOf("/")+1);
                data = new Data(name, path, type);
                data.setID(id);
                result.add(data);
                id++;

                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }



}
