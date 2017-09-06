package com.github.nds842.sqlfirst.tracksample;


import com.github.nds842.sqlfirst.apc.SqlSource;
import com.github.nds842.sqlfirst.apc.SqlSourceFile;
import com.github.nds842.sqlfirst.base.SQLSourceType;
import com.github.nds842.sqlfirst.tracksample.dto.CheckTrackCodeReq;
import com.github.nds842.sqlfirst.tracksample.dto.CheckTrackCodeRes;
import com.github.nds842.sqlfirst.tracksample.dto.InsertTrackCodeReq;

import java.sql.Connection;
import java.util.List;

@SqlSourceFile(implement = true)
public interface TrackCodeSample {

    /**
     * Sql for this method is contained in file
     */
    @SqlSource(sqlSourceType = SQLSourceType.FILE, sqlSourceFile = "sql/CheckTrackCode.sql")
    List<CheckTrackCodeRes> checkTrackCode(CheckTrackCodeReq req, Connection conn);

    /**
     * INSERT into trackcode (id, code, parcelid, dateassign)
     * VALUES (:id__l,:codestring__s,:parcelid__l,:dateassign_d)
     */
    @SqlSource
    void insertTrackCode(InsertTrackCodeReq req, Connection conn);

}
