package com.github.nds842.sqlfirst.plainsqlsample.queryexecutor;

import java.sql.SQLException;


public class RTSQLException extends RuntimeException {
    public RTSQLException(SQLException ex) {
        super(ex);
    }
}
