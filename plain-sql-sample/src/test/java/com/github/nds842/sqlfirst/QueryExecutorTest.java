package com.github.nds842.sqlfirst;

import com.github.nds842.sqlfirst.base.BaseDto;
import com.github.nds842.sqlfirst.plainsqlsample.queryexecutor.DefaultNamedParamQueryExecutor;
import com.github.nds842.sqlfirst.plainsqlsample.queryexecutor.QueryExecutor;
import com.github.nds842.sqlfirst.queryexecutor.QueryResultTransformer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryExecutorTest {

    @Test
    public void test() {

        List<String> actualList = new ArrayList<>();

        QueryExecutor qr = prepareQueryExecutor(actualList);

        BaseDto testDto = new BaseDto() {
            @Override
            public Map<String, Object> toMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("p1_val__i", 123);
                map.put("p2_val__l", 456L);
                return map;
            }
        };
        qr.executeUpdate("update sometable set p1=:p1_val__i, p2=:p2_val__l", testDto, null);

        QueryResultTransformer<BaseDto> tr = (rs, rsNames) -> testDto;
        qr.executeQuery("select * from sometable where p1=:p1_val__i, p2=:p2_val__l", testDto, tr, null);
        Assert.assertEquals(actualList.toString(), "[update sometable set p1=?, p2=?, [123, 456], select * from sometable where p1=?, p2=?, [123, 456]]");
    }

    private QueryExecutor prepareQueryExecutor(final List<String> actualList) {
        return new DefaultNamedParamQueryExecutor() {
            @Override
            public QueryRunner getQueryRunner() {
                return new QueryRunner() {
                    @Override
                    public int update(Connection conn, String sql, Object[] arr) throws SQLException {
                        actualList.add(sql);
                        actualList.add(Arrays.asList(arr).toString());
                        return 0;
                    }

                    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object[] arr) throws SQLException {
                        actualList.add(sql);
                        actualList.add(Arrays.asList(arr).toString());
                        return null;
                    }
                };
            }
        };
    }
}
