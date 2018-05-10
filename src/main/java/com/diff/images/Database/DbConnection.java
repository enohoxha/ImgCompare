package com.diff.images.Database;

import java.sql.*;

public class DbConnection {

    Connection con;

    public DbConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        con= DriverManager.getConnection("jdbc:mysql://localhost:3306/images","root","26ornela26");
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("select * from emp");
        return rs;
    }
}
