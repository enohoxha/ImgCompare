package com.diff.images;
import com.diff.images.Config.Config;
import com.diff.images.Database.DbConnection;
import com.diff.images.Models.ImageModel;
import com.diff.images.Services.ImageCompareService;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageProcessor {


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        int threadNumber = Integer.parseInt(Config.getProperty("number_of_threads"));
        DbConnection db = new DbConnection();

        // count rows
        ResultSet resultSet = db.executeQuery("SELECT count(*) as total FROM photos", false);
        int rowNumber = 0;
        while (resultSet.next()) {
            rowNumber = resultSet.getInt("total");
        }

        // Find number of rows for each thread
        int recordsPerRow = rowNumber / threadNumber;

        ArrayList<ImageModel> images = new ArrayList<ImageModel>(recordsPerRow);

       // String query = "SELECT * FROM photos limit " + recordsPerRow + " offset " + i * recordsPerRow;
        String query2 = "SELECT * FROM photos ";
        ResultSet resultSetRows = db.executeQuery(query2, false);

        while (resultSetRows.next()) {
            images.add(new ImageModel(resultSetRows.getInt("id"), resultSetRows.getString("img")));
        }

        // loop images
        for (int i = 0; i < images.size(); i++){
            ImageModel imageModel = images.get(i);
            for (int j = 0; j < threadNumber; j++){
                ImageCompareService compareService = new ImageCompareService(imageModel, images.subList(j * recordsPerRow, j*recordsPerRow + recordsPerRow), "t-"+j, db);
                compareService.start();
            }
            for (int j = 0; j < threadNumber; j++){
                Thread t = getThreadByName("t-"+j);
                if(t != null)
                    t.join();
            }
        }


    }

    public static Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }

}
