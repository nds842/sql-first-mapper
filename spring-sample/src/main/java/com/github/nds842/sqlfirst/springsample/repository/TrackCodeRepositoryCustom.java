package com.github.nds842.sqlfirst.springsample.repository;

import com.github.nds842.sqlfirst.apc.SqlSource;
import com.github.nds842.sqlfirst.apc.SqlSourceFile;
import com.github.nds842.sqlfirst.base.SQLSourceType;
import com.github.nds842.sqlfirst.springsample.repository.dto.CheckTrackCodeReq;
import com.github.nds842.sqlfirst.springsample.repository.dto.CheckTrackCodeRes;
import com.github.nds842.sqlfirst.springsample.repository.dto.InsertTrackCode4Req;
import com.github.nds842.sqlfirst.springsample.repository.dto.InsertTrackCodeReq;

import java.util.List;

@SqlSourceFile(baseDaoClassName = "com.github.nds842.sqlfirst.springsample.repository.TrackCodeRepositoryParent")
public interface TrackCodeRepositoryCustom {
    
    /**
     * Sql for this method is contained in file
     */
    @SqlSource(sqlSourceType = SQLSourceType.FILE, sqlSourceFile = "sql/CheckTrackCode.sql")
    List<CheckTrackCodeRes> checkTrackCode(CheckTrackCodeReq req);
    
    /**
     * INSERT into trackcode (id, code, parcelid, dateassign)
     * VALUES (:id__l,:codestring__s,:parcelid__l,:dateassign_d)
     */
    @SqlSource
    void insertTrackCode(InsertTrackCodeReq req);
    
    /**
     * INSERT into trackcode (id, code, parcelid, dateassign)
     * VALUES (:id__l,:codestring__s,:parcelid__l,:dateassign_d)
     */
    @SqlSource
    void insertTrackCode4(InsertTrackCode4Req req);
    
    /**
     * Method to be implemented in parent of generated repository impl class
     */
    String countSampleLetters();
    
}
    
