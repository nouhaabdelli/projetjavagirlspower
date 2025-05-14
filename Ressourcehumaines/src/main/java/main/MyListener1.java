package main;

import entities.Formation;

import java.sql.SQLException;

public interface MyListener1 {
    public void onClickListener(Formation post) throws SQLException;
}
