package com.github.nds842.sqlfirst.springsample.repository;

import com.github.nds842.sqlfirst.apc.SqlSource;
import com.github.nds842.sqlfirst.apc.SqlSourceFile;
import com.github.nds842.sqlfirst.base.SQLSourceType;
import com.github.nds842.sqlfirst.springsample.repository.dto.CheckTrackCodeReq;
import com.github.nds842.sqlfirst.springsample.repository.dto.CheckTrackCodeRes;
import com.github.nds842.sqlfirst.springsample.repository.dto.UpdateTrackCodeReq;

import java.util.List;

@SqlSourceFile(baseDaoClassName = "com.github.nds842.sqlfirst.springsample.repository.TrackCodeRepositoryParent")
public interface TrackCodeRepositoryCustom {
    
    /**
     * Sql for this method is contained in file
     */
    @SqlSource(sqlSourceType = SQLSourceType.FILE, sqlSourceFile = "sql/CheckTrackCode.sql")
    List<CheckTrackCodeRes> checkTrackCode(CheckTrackCodeReq req);
    
    /**
     * UPDATE track_code SET track_code = :codestring__s
     * WHERE postage_id = :postage_id__l
     */
    @SqlSource
    void updateTrackCode(UpdateTrackCodeReq req);
    
    /**
     * Method to be implemented in parent of generated repository impl class
     */
    String countSampleLetters();
    
}
    
