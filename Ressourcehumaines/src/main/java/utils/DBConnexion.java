/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TECHNOPC
 */
public class DBConnexion {
    String password="";
    String login="root";
    String url;
    Connection connection;
    public static DBConnexion instance;

    DBConnexion(){
        this.url = "jdbc:mysql://localhost:3306/workbridge2";
        try {
            connection = DriverManager.getConnection(url,login,password);
            System.out.println("connected successfully");
        }catch(SQLException  ex)
        {
            Logger.getLogger(DBConnexion.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    public Connection getCnx(){return connection;}

    public static DBConnexion getInstance(){
        if(instance==null){
            instance = new DBConnexion();
        }
        return instance;
    }
}
