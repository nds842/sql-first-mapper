package com.github.nds842.sqlfirst.springsample.system;

import com.github.nds842.sqlfirst.base.BaseDto;
import com.github.nds842.sqlfirst.base.TemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class SqlFirstSpringQueryExecutor  {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    public <T extends BaseDto, V extends BaseDto> List<T> executeQuery(String queryTemplate, V req, RowMapper<T> rowMapper) {
        Map<String, Object> queryParamMap = req.toMap();
        String query = TemplateUtils.prepareTemplate(queryTemplate, queryParamMap);
        return namedParameterJdbcTemplate.query(query, queryParamMap, rowMapper);
    }
    
    public <T extends BaseDto> void execute(String queryTemplate, T req) {
        Map<String, Object> queryParamMap = req.toMap();
        String query = TemplateUtils.prepareTemplate(queryTemplate, queryParamMap);
        namedParameterJdbcTemplate.update(query, queryParamMap);
    }
    
    public <T extends BaseDto> List<T> executeQuery(String query, RowMapper<T> rowMapper) {
        return namedParameterJdbcTemplate.query(query, rowMapper);
    }
    
    public void execute(String query) {
        namedParameterJdbcTemplate.update(query, Collections.emptyMap());
    }
    
    
    /**
     * Get velocity template by query name and package name, reads file from resources and returns as string
     *
     * @param packageName name of package
     * @param queryName name of query
     * @return string with velocity template file contents
     */
    public static String getTemplate(String packageName, String queryName) {
        return TemplateUtils.getTemplate(packageName, queryName);
    }
}
