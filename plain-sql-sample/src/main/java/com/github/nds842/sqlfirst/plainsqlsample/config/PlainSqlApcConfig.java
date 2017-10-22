package com.github.nds842.sqlfirst.plainsqlsample.config;

import com.github.nds842.sqlfirst.apc.DaoType;
import com.github.nds842.sqlfirst.apc.SqlFirstApcConfig;

@SqlFirstApcConfig(
        daoType = DaoType.PLAIN_SQL,
        baseDaoClassName = "com.github.nds842.sqlfirst.plainsqlsample.queryexecutor.BaseDao"
)
public interface PlainSqlApcConfig {
}
