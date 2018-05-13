package com.diff.images.Database;

import com.diff.images.Config.Config;
import com.diff.images.Models.CompareModel;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DbConnection {

    Connection con;

    public DbConnection() throws SQLException, ClassNotFoundException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con= DriverManager.getConnection(Config.getProperty("mysql_url"),Config.getProperty("mysql_user"),Config.getProperty("mysql_password"));
    }

    public ResultSet executeQuery(String query, Boolean update) throws SQLException {
        Statement stmt=con.createStatement();
        ResultSet rs = null;
        if(update)
            stmt.executeUpdate(query);
        else
            rs = stmt.executeQuery(query);

        return rs;
    }

    public boolean compare(int idOne, int idTwo) throws SQLException {
        String query = "SELECT DISTINCT image_id, image_id_2 FROM `compare` WHERE ((image_id_2 = "+idOne+" || image_id_2 = "+idTwo+")" +
                " and sim > 0.95) \n" +
                "OR\n" +
                "((image_id = "+idOne+" || image_id = "+idTwo+") and sim > 0.95)";

        Statement stmt=con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<CompareModel> compareModels = new ArrayList<CompareModel>();
        while (rs.next()){
            compareModels.add(new CompareModel(rs.getInt("image_id"), rs.getInt("image_id_2")));
        }


        for (int i = 0; i < compareModels.size(); i++){
            for (int j = 0; j < compareModels.size(); j++){
                if(compareModels.get(i).getImgOne() == idOne && compareModels.get(j).getImgOne() == idTwo){
                    if (compareModels.get(i).getImgTwo() == compareModels.get(j).getImgTwo()){
                        return false;
                    }
                }if(compareModels.get(i).getImgTwo() == idOne && compareModels.get(j).getImgTwo() == idTwo){
                    if (compareModels.get(i).getImgOne() == compareModels.get(j).getImgOne()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
