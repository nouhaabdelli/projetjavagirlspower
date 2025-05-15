package main;

import entities.Certificat;

import java.sql.SQLException;

public interface MyListener2 {
    public void onClickListener(Certificat certificat) throws SQLException;


}