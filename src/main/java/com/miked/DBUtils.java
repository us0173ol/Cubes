package com.miked;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by miked on 11/29/2016.
 */
public class DBUtils {

    static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    static String DB_NAME = "cubes";
    static final String USER = "mikey";
    static final String PASSWORD = "mikedodge";

    static void registerDriver(){

        try{
            String Driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(Driver);
        }catch (ClassNotFoundException cnfe){
            System.out.println("No database drivers found. Ensure you've imported MySQL dependencies with Maven, \n" +
                    "and if running from the command line, that you've copied the dependencies to your project. \nExiting Program");
            System.exit(-1);
        }
    }
        //I just copy and pasted this entire class from PrimaryKeyExample
    static Connection getConnection() {
        try{
            return DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASSWORD);

        }catch (SQLException sqle){
            System.out.println("Unable to connect to database.");
            System.out.println("Verify that your MySQL is running\n" +
                    "The " + DB_NAME + " database exists\n" +
                    "You have the correct username and password\n" +
                    "You have granted permissions to your user\n" +
                    "Exiting Program");

            sqle.printStackTrace();

            System.exit(-1);
        }
        return null;
    }
}
