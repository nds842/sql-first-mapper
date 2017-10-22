package com.github.nds842.sqlfirst.springsample.repository;


import com.github.nds842.sqlfirst.apc.SqlSource;
import com.github.nds842.sqlfirst.base.SQLSourceType;
import com.github.nds842.sqlfirst.springsample.repository.dto.FindSampleLetterNoSenderRes;
import com.github.nds842.sqlfirst.springsample.repository.dto.FindSampleLetterReq;
import com.github.nds842.sqlfirst.springsample.repository.dto.FindSampleLetterRes;
import com.github.nds842.sqlfirst.springsample.repository.dto.InsertSampleLetterReq;

import java.util.List;

public interface LetterRepositoryCustom {
    
    /**
     *  SELECT
     *  letter.height          height__l,
     *  letter.width           width__l,
     *  letter.date_send       send_date__d,
     *  letter.weight          weight__l,
     *  letter.sender_name     sender_name__s
     *  FROM letter WHERE
     *    sender_name = :sender__s
     *    #if(${width__l})       AND letter.width  = :width__l       #end
     *    #if(${height__l})      AND letter.height = :height__l      #end
     *    #if(${start_date__d})  AND letter.date_send  between :start_date__d and :end_date__d #end
     */
    @SqlSource(sqlSourceType = SQLSourceType.JAVADOC)
    List<FindSampleLetterRes> findSampleLetter(FindSampleLetterReq req);
    
    /**
     *  SELECT
     *  letter.height          height__l,
     *  letter.width           width__l,
     *  letter.date_send       send_date__d,
     *  letter.weight          weight__l,
     *  letter.sender_name     sender_name__s
     *  FROM letter WHERE
     *    sender_name IS NULL
     */
    @SqlSource(sqlSourceType = SQLSourceType.JAVADOC)
    List<FindSampleLetterNoSenderRes> findSampleLetterNoSender();
    
    
    /**
     * INSERT into letter (height,width,date_send,weight,sender_name) VALUES (
     * :height__l,:width__l,:send_date__d,:weight__l,:sender_name__s)
     */
    @SqlSource
    void insertSampleLetter(InsertSampleLetterReq req);
    
    /**
     * DELETE FROM LETTER
     */
    @SqlSource
    void deleteSampleLetter();
    
    
}
