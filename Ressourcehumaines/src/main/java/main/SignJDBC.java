package main;

import entities.Per;


import java.sql.SQLException;

public class SignJDBC {

    public static void main(String[] args) {


        try {
            System.out.println(ps.readAll());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}
