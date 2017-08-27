package com.github.nds842.sqlfirst.tracksample;


import com.github.nds842.sqlfirst.apc.SqlSource;
import com.github.nds842.sqlfirst.apc.SqlSourceFile;
import com.github.nds842.sqlfirst.tracksample.dto.CheckTrackCodeReq;
import com.github.nds842.sqlfirst.tracksample.dto.CheckTrackCodeRes;
import com.github.nds842.sqlfirst.tracksample.dto.InsertTrackCodeReq;

import java.sql.Connection;
import java.util.List;

@SqlSourceFile(implement = true)
public interface TrackCodeSample {

    /**
     * SELECT
     * trackcode.id           id__l
     * trackcode.code         codestring__s,
     * trackcode.parcelid     parcelid__l,
     * trackcode.dateassign   dateassign_d
     * FROM trackcode
     * WHERE parcelid = :parcelid__l
     */
    @SqlSource
    List<CheckTrackCodeRes> checkTrackCode(CheckTrackCodeReq req, Connection conn);

    /**
     * INSERT into trackcode (id, code, parcelid, dateassign)
     * VALUES (:id__l,:codestring__s,:parcelid__l,:dateassign_d)
     */
    @SqlSource
    void insertTrackCode(InsertTrackCodeReq req, Connection conn);

}
