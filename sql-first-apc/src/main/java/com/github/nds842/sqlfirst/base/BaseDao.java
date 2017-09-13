package com.github.nds842.sqlfirst.base;


import com.github.nds842.sqlfirst.queryexecutor.DefaultNamedParamQueryExecutor;
import com.github.nds842.sqlfirst.queryexecutor.QueryResultTransformer;
import com.github.nds842.sqlfirst.queryexecutor.RTSQLException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Parent of generated Dao classes
 */
public class BaseDao {

    //TODO add query executor factory
    private QueryExecutor queryExecutor = new DefaultNamedParamQueryExecutor();

    protected void executeUpdate(String noParamQuery, Connection conn) {
        queryExecutor.executeUpdate(noParamQuery, conn);
    }

    protected <T extends BaseDto> void executeUpdate(String reqQuery, T req, Connection conn) {
        queryExecutor.executeUpdate(reqQuery, req, conn);
    }

    protected <T extends BaseDto> List<T> executeQuery(Connection conn, String resQuery, QueryResultTransformer<T> transformer) {
        return queryExecutor.executeQuery(resQuery, transformer, conn);
    }

    protected <T extends BaseDto, V extends BaseDto> List<T> executeQuery(String reqResQuery, V req, QueryResultTransformer<T> transformer, Connection conn) {
        return queryExecutor.executeQuery(reqResQuery, req, transformer, conn);
    }

    protected String getTemplate(String packageName, String queryName) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(packageName.replace(".", "/") + "/sql/" + queryName + ".sql");
        try {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Long getLongSafely(ResultSet rs, Set<String> rsNames, String columnName) {
        if (!rsNames.contains(columnName.toUpperCase())) {
            return null;
        }
        try {
            long val = rs.getLong(columnName);
            return rs.wasNull() ? null : val;
        } catch (SQLException ex) {
            throw new RTSQLException(ex);
        }
    }

    protected Integer getIntegerSafely(ResultSet rs, Set<String> rsNames, String columnName) {
        if (!rsNames.contains(columnName.toUpperCase())) {
            return null;
        }
        try {
            int val = rs.getInt(columnName);
            return rs.wasNull() ? null : val;
        } catch (SQLException ex) {
            throw new RTSQLException(ex);
        }
    }


    protected String getStringSafely(ResultSet rs, Set<String> rsNames, String columnName) {
        if (!rsNames.contains(columnName.toUpperCase())) {
            return null;
        }
        try {
            return rs.getString(columnName);
        } catch (SQLException ex) {
            throw new RTSQLException(ex);
        }
    }

    protected Date getDateSafely(ResultSet rs, Set<String> rsNames, String columnName) {
        if (!rsNames.contains(columnName.toUpperCase())) {
            return null;
        }
        try {
            Timestamp val = rs.getTimestamp(columnName);
            return val == null ? null : new Date(val.getTime());
        } catch (SQLException ex) {
            throw new RTSQLException(ex);
        }
    }
}
