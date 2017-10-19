package com.github.nds842.sqlfirst.springsample.repository.impl;

import com.github.nds842.sqlfirst.base.BaseDao;
import com.github.nds842.sqlfirst.base.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class BaseSpringDao extends BaseDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    protected <T extends BaseDto, V extends BaseDto> List<T> executeQuery(String queryTemplate, V req, RowMapper<T> rowMapper) {
        Map<String, Object> queryParamMap = req.toMap();
        String query = prepareQueryWithNamedParameters(queryTemplate, queryParamMap);
        return namedParameterJdbcTemplate.query(query, queryParamMap, rowMapper);
    }
    
    protected <T extends BaseDto> void execute(String queryTemplate, T req) {
        Map<String, Object> queryParamMap = req.toMap();
        String query = prepareQueryWithNamedParameters(queryTemplate, queryParamMap);
        namedParameterJdbcTemplate.update(query, queryParamMap);
    }
    
    protected <T extends BaseDto> List<T> executeQuery(String query, RowMapper<T> rowMapper) {
        return namedParameterJdbcTemplate.query(query, rowMapper);
    }
    
    protected void execute(String query) {
        namedParameterJdbcTemplate.update(query, Collections.emptyMap());
    }
}
