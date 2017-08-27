package com.github.nds842.sqlfirst.queryexecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public interface QueryResultTransformer<T> {

    T createDto(ResultSet rs, Set<String> rsColumnNames) throws SQLException;

}
