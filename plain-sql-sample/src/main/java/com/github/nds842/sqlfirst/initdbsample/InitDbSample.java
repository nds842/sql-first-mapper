package com.github.nds842.sqlfirst.initdbsample;


import com.github.nds842.sqlfirst.apc.SqlSource;
import com.github.nds842.sqlfirst.apc.SqlSourceFile;

import java.sql.Connection;

@SqlSourceFile(implement = true)
public interface InitDbSample {

    /**
     * CREATE TABLE LETTER (
     * id               number(31)
     * ,height          number(10,2)
     * ,width           number(10,2)
     * ,date_send       datetime
     * ,weight          number(10,2)
     * ,sender_name     varchar(256)
     * );
     *
     * CREATE TABLE PARCEL (
     *  id              number(31)
     * ,height          number(10,2)
     * ,width           number(10,2)
     * ,date_send       datetime
     * ,weight          number(10,2)
     * ,sender_name     varchar(256)
     * );
     */
    @SqlSource
    void createTables(Connection conn);

}
