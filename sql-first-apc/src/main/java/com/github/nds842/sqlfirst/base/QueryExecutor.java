package com.github.nds842.sqlfirst.base;

import com.github.nds842.sqlfirst.queryexecutor.QueryResultTransformer;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.util.List;

/**
 * Interface for QueryExecutor, that will process velocity template with  parameters, connection and result transformer
 */
public interface QueryExecutor {
    
    /**
     * Execute query that has neither request nor response
     *
     * @param noParamQuery query as velocity template string, it may depend on database type or other context parameters
     * @param conn connection to execute query
     */
    void executeUpdate(String noParamQuery, Connection conn);
    
    /**
     * Execute query that has no response parameters
     *
     * @param reqQuery query as velocity template string
     * @param req request query parameters
     * @param conn connection to execute query
     * @param <T> type of request
     */
    <T extends BaseDto> void executeUpdate(String reqQuery, T req, Connection conn);
    
    /**
     * Execute query that has no request parameters
     *
     * @param resQuery query as velocity template string
     * @param transformer transformer for query result
     * @param conn connection to execute query
     * @param <T> type of result
     * @return list of query results
     */
    <T extends BaseDto> List<T> executeQuery(String resQuery, QueryResultTransformer<T> transformer, Connection conn);
    
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
    <T extends BaseDto, V extends BaseDto> List<T> executeQuery(String reqResQuery, V req, QueryResultTransformer<T> transformer, Connection conn);
    
    /**
     * Query runner
     *
     * @return instance of query runner
     */
    default QueryRunner getQueryRunner() {
        return new QueryRunner();
    }

}
