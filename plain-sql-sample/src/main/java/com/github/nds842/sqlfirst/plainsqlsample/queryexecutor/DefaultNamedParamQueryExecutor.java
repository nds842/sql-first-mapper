package com.github.nds842.sqlfirst.plainsqlsample.queryexecutor;

import com.github.nds842.sqlfirst.base.BaseDto;
import com.github.nds842.sqlfirst.queryexecutor.QueryResultTransformer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DefaultNamedParamQueryExecutor implements QueryExecutor {

    private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("(?!\\B'[^']*)(:\\w+)(?![^']*'\\B)");

    @Override
    public void executeUpdate(String query, Connection conn) {
        QueryRunner qRunner = getQueryRunner();
        try {
            qRunner.update(conn, query);
        } catch (SQLException e) {
            throw new RTSQLException(e);
        }
    }

    @Override
    public <T extends BaseDto> void executeUpdate(String query, T req, Connection conn) {
        Map<String, Object> queryParamMap = req == null ? Collections.emptyMap() : req.toMap();
        Matcher matcher = QUERY_PARAM_PATTERN.matcher(query);
        List<String> paramNameList = getReqParamNames(matcher);
        Object[] arr = getReqParamValues(queryParamMap, paramNameList);
        QueryRunner qRunner = getQueryRunner();
        try {
            qRunner.update(conn, matcher.replaceAll("?"), arr);
        } catch (SQLException e) {
            throw new RTSQLException(e);
        }
    }

    @Override
    public <T extends BaseDto> List<T> executeQuery(String query, QueryResultTransformer<T> transformer, Connection conn) {
        ResultSetHandler<List<T>> rsHandler = getListResultSetHandler(transformer);
        QueryRunner qRunner = getQueryRunner();
        try {
            return qRunner.query(conn, query, rsHandler);
        } catch (SQLException e) {
            throw new RTSQLException(e);
        }
    }


    private Set<String> getColumnNames(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Set<String> columnNames = new HashSet<>(columnCount);
        for (int i = 1; i < columnCount + 1; i++) {
            columnNames.add(metaData.getColumnLabel(i).toUpperCase());
        }
        return columnNames;
    }

    @Override
    public <T extends BaseDto, V extends BaseDto> List<T> executeQuery(String query, V req, QueryResultTransformer<T> transformer, Connection conn) {
        Map<String, Object> queryParamMap = req == null ? Collections.emptyMap() : req.toMap();
        Matcher matcher = QUERY_PARAM_PATTERN.matcher(query);
        List<String> paramNameList = getReqParamNames(matcher);
        Object[] arr = getReqParamValues(queryParamMap, paramNameList);
        ResultSetHandler<List<T>> rsHandler = getListResultSetHandler(transformer);

        QueryRunner qRunner = getQueryRunner();
        try {
            return qRunner.query(conn, matcher.replaceAll("?"), rsHandler, arr);
        } catch (SQLException e) {
            throw new RTSQLException(e);
        }
    }

    private <T extends BaseDto> ResultSetHandler<List<T>> getListResultSetHandler(QueryResultTransformer<T> transformer) {
        return rs -> {
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add(transformer.createDto(rs, getColumnNames(rs)));
            }
            return list;
        };
    }

    private Object[] getReqParamValues(Map<String, Object> queryParamMap, List<String> paramNameList) {
        Object[] arr = new Object[paramNameList.size()];
        for (int i = 0; i < paramNameList.size(); i++) {
            arr[i] = queryParamMap.get(paramNameList.get(i));
        }
        return arr;
    }

    private List<String> getReqParamNames(Matcher matcher) {
        List<String> paramNameList = new ArrayList<>();
        while (matcher.find()) {
            paramNameList.add(matcher.group().substring(1));
        }
        return paramNameList;
    }
}
