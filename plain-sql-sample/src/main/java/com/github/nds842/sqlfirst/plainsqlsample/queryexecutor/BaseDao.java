package com.github.nds842.sqlfirst.plainsqlsample.queryexecutor;


import com.github.nds842.sqlfirst.base.BaseDto;
import com.github.nds842.sqlfirst.base.TemplateUtils;
import com.github.nds842.sqlfirst.queryexecutor.QueryResultTransformer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Parent for generated Dao classes
 */
public abstract class BaseDao {

    //TODO add query executor factory
    private QueryExecutor queryExecutor = new DefaultNamedParamQueryExecutor();
    
    /**
     * Execute query that has neither request nor response
     *
     * @param noParamQuery query as velocity template string, it may depend on database type or other context parameters
     * @param conn connection to execute query
     */
    protected void executeUpdate(String noParamQuery, Connection conn) {
        queryExecutor.executeUpdate(noParamQuery, conn);
    }
    
    /**
     * Execute query that has no response parameters
     *
     * @param reqQuery query as velocity template string
     * @param req request query parameters
     * @param conn connection to execute query
     * @param <V> type of request
     */
    protected <V extends BaseDto> void executeUpdate(String reqQuery, V req, Connection conn) {
        queryExecutor.executeUpdate(TemplateUtils.prepareTemplate(reqQuery, req.toMap()), req, conn);
    }
    
    /**
     * Execute query that has no request parameters
     *
     * @param resQuery query as velocity template string
     * @param transformer transformer for query result
     * @param conn connection to execute query
     * @param <T> type of result
     * @return list of query results
     */
    protected <T extends BaseDto> List<T> executeQuery(String resQuery, QueryResultTransformer<T> transformer, Connection conn) {
        return queryExecutor.executeQuery(resQuery, transformer, conn);
    }
    
    /**
     * Execute query that has request and response
     *
     * @param reqResQuery query as velocity template string
     * @param req request query parameters
     * @param transformer transformer for query result
     * @param conn connection to execute query
     * @param <T> type of result
     * @param <V> type of request
     *
     * @return list of query results
     */
    protected <T extends BaseDto, V extends BaseDto> List<T> executeQuery(
            String reqResQuery,
            V req,
            QueryResultTransformer<T> transformer,
            Connection conn
    ) {
        return queryExecutor.executeQuery(TemplateUtils.prepareTemplate(reqResQuery, req.toMap()), req, transformer, conn);
    }
    
    /**
     * Get velocity template by query name and package name, reads file from resources and returns as string
     *
     * @param packageName name of package
     * @param queryName name of query
     * @return string with velocity template file contents
     */
    protected String getTemplate(String packageName, String queryName) {
        return TemplateUtils.getTemplate(packageName, queryName);
    }
    
    /**
     * Get long from result set by column name, dynamic nature of SQL query allows result
     * to contain different set of result columns depending on query parameters
     *
     * @param rs result set to get long value from
     * @param rsNames actual names of columns returned from query
     * @param columnName name of column to get from result set
     * @return long value obtained from result set by columnName
     */
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
    
    /**
     * Get integer from result set by column name, dynamic nature of SQL query allows result
     * to contain different set of result columns depending on query parameters
     *
     * @param rs result set to get string from
     * @param rsNames actual names of columns returned from query
     * @param columnName name of column to get from result set
     * @return string value obtained from result set by columnName
     */
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
    
    /**
     * Get string from result set by column name, dynamic nature of SQL query allows result
     * to contain different set of result columns depending on query parameters
     *
     * @param rs result set to get string from
     * @param rsNames actual names of columns returned from query
     * @param columnName name of column to get from result set
     * @return string value obtained from result set by columnName
     */
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
    
    /**
     * Get date from result set by column name, dynamic nature of SQL query allows result
     * to contain different set of result columns depending on query parameters
     *
     * @param rs result set to get date from
     * @param rsNames actual names of columns returned from query
     * @param columnName name of column to get from result set
     * @return date value obtained from result set by columnName
     */
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
    
    //TODO add get***Safely for other types
    
}
