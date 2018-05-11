package com.diff.images.Database;

import com.diff.images.Config.Config;

import java.io.IOException;
import java.sql.*;

public class DbConnection {

    Connection con;

    public DbConnection() throws SQLException, ClassNotFoundException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
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
}
