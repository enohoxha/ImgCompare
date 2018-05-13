package com.diff.images;

import com.diff.images.Config.Config;
import com.diff.images.Database.DbConnection;
import com.diff.images.Models.ClustersModel;
import com.diff.images.Models.ImageModel;
import com.diff.images.Services.ImageCompareService;
import javax.swing.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageProcessor {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        System.gc();
        double startTime = System.currentTimeMillis();
        // Initialize db
        DbConnection db = new DbConnection();

        // Initialize threads used
        int threadNumber = Integer.parseInt(Config.getProperty("number_of_threads"));
        ExecutorService writeService = Executors.newCachedThreadPool();
        ExecutorService compareService = Executors.newCachedThreadPool();

        // Initialize files
        File file = new File(Config.getProperty("image_home_dir") + "clusters.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        FileInputStream fileInputStream = new FileInputStream(new File(Config.getProperty("image_home_dir") + "clusters.txt"));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        //objectInputStream.reset();
        objectOutputStream.reset();

        // Initialize container variables
        HashMap<String, ClustersModel[]> clustersModels = new HashMap<String, ClustersModel[]>();
        HashMap<String, ClustersModel[]> clusterModelsFromFiles = new HashMap<String, ClustersModel[]>();

        // Initialize helper varialbles
        int end = 0;
        int start = 0;

        // count rows
        if (Integer.parseInt(Config.getProperty("clear_compare_table")) == 1) {
            db.executeQuery("TRUNCATE TABLE compare", true);
        } // count rows
        if (Integer.parseInt(Config.getProperty("execute_import")) == 0) {
            ResultSet resultSet = db.executeQuery("SELECT count(*) as total FROM photos", false);
            int rowNumber = 0;
            while (resultSet.next()) {
                rowNumber = resultSet.getInt("total");
            }

            if(threadNumber > rowNumber - (rowNumber / 2 + (rowNumber/4))){
                throw new ArithmeticException("Thread Number Not valid");
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

            for (int i = 1; i <= threadNumber; i++) {
                end = start + recordsPerRow;
                List<ImageModel> subList = images.subList(start, end);
                ImageCompareService imageCompareService = new ImageCompareService(subList, "Thread-" + i, db, objectOutputStream, objectInputStream, "dry_run");
                writeService.execute(imageCompareService);
                start += recordsPerRow;

            }
            writeService.shutdown();
            writeService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            System.out.println("Finished");
            objectOutputStream.close();
            Thread.sleep(2000);
            int count = 0;
            while (count < rowNumber) {
                try {
                    count++;
                    clustersModels = (HashMap<String, ClustersModel[]>) objectInputStream.readObject();
                    clusterModelsFromFiles.putAll(clustersModels);
                } catch (EOFException | OptionalDataException  | StreamCorruptedException e){
                    System.out.println("Error " + count);
                    throw new IOException("File clusters.txt is corrupted delete it");

                }
            }
            end = 0;
            for (int j = 1; j <= threadNumber; j++) {
                List<ImageModel> subList = images.subList(end, images.size());
                end = (recordsPerRow - recordsPerRow / 3) - 1;
                if (end > subList.size() || j == threadNumber)
                    end = subList.size();
                ImageCompareService imageCompareService = new ImageCompareService(subList, "Thread-" + j, end, clusterModelsFromFiles, db, "live_run");
                compareService.execute(imageCompareService);
                end = (end * j);
            }
            compareService.shutdown();
            compareService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            double endTime = System.currentTimeMillis();

            System.out.println("****************************************************************************************************************");
            System.out.println("All Algorithm Completed");
            System.out.println("Time (sec): " + (endTime - startTime) / 1000 + "\n" +
                    "Time (min): " + ((endTime - startTime) / 1000) / 60 + "\n"
                    );
            System.out.println("****************************************************************************************************************");
            fileInputStream.close();
            fileOutputStream.close();
            file.delete();
        } else {
            helperFunction();
        }


    }


    public static void helperFunction() throws IOException, SQLException, ClassNotFoundException {
        File directory = new File(Config.getProperty("image_home_dir"));
        Map<String, ImageIcon> iconMap = new HashMap<String, ImageIcon>();
        DbConnection db = new DbConnection();
        db.executeQuery("TRUNCATE TABLE photos", true);
        for (File file : directory.listFiles()) {
            // could also use a FileNameFilter
            if (file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg")
                    || file.getName().toLowerCase().endsWith(".jpeg")) {

                db.executeQuery("insert into photos(img) values('" + file.getName() + "')", true);
            }
        }

        System.out.println("Import completed");
    }

}
