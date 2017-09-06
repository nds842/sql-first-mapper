package com.github.nds842.sqlfirst.base;

import com.github.nds842.sqlfirst.queryexecutor.QueryResultTransformer;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.util.List;

public interface QueryExecutor {

    void executeUpdate(String noParamQuery, Connection conn);

    <T extends BaseDto> void executeUpdate(String reqQuery, T req, Connection conn);

    <T extends BaseDto> List<T> executeQuery(String resQuery, QueryResultTransformer<T> resQueryResClass, Connection conn);

    <T extends BaseDto, V extends BaseDto> List<T> executeQuery(String reqResQuery, V req, QueryResultTransformer<T> reqResQueryResClass, Connection conn);

    default QueryRunner getQueryRunner() {
        return new QueryRunner();
    }

}
