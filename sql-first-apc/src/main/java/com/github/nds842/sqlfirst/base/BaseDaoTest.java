package com.github.nds842.sqlfirst.base;


import java.sql.Connection;

public class BaseDaoTest {

    protected <T extends  BaseDto>void fillRequest(T req) {
    }

    protected Connection getConnection() {
        return null;
    }
}
