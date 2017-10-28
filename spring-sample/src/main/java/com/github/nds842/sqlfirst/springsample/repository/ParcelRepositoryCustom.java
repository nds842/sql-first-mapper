package com.github.nds842.sqlfirst.springsample.repository;


import com.github.nds842.sqlfirst.apc.SqlSource;
import com.github.nds842.sqlfirst.apc.SqlSourceFile;
import com.github.nds842.sqlfirst.springsample.common.SenderNameItem;
import com.github.nds842.sqlfirst.springsample.repository.dto.FindSampleParcelReq;
import com.github.nds842.sqlfirst.springsample.repository.dto.FindSampleParcelRes;
import com.github.nds842.sqlfirst.springsample.repository.dto.InsertSampleParcelReq;

import java.util.List;

@SqlSourceFile(targetClassName = "com.github.nds842.sqlfirst.springsample.repository.ParcelRepositoryApc")
public interface ParcelRepositoryCustom {
    
    /**
     * SELECT
     * parcel.id              id__l,
     * parcel.height          height__l,
     * parcel.width           width__l,
     * parcel.date_send       send_date__d,
     * parcel.weight          weight__l,
     * parcel.sender_name     sender_name__s
     * FROM parcel WHERE
     * sender_name = :sender__s
     * #if(${width__l})       AND parcel.width  = :width__l       #end
     * #if(${weight__l})      AND parcel.weight = :weight__l      #end
     * #if(${height__l})      AND parcel.height = :height__l      #end
     * #if(${start_date__d})  AND parcel.date_send  between :start_date__d and :end_date__d #end
     */
    @SqlSource(resImpl = SenderNameItem.class)
    List<FindSampleParcelRes> findSampleParcel(FindSampleParcelReq req);
    
    /**
     * INSERT into parcel (id, height,width,date_send,weight,sender_name) VALUES (
     * :id__l,:height__l,:width__l,:send_date__d,:weight__l,:sender_name__s)
     */
    @SqlSource(skipTest = true)
    void insertSampleParcel(InsertSampleParcelReq req);
    
    
    /**
     * DELETE FROM parcel
     */
    @SqlSource
    void deleteAllSampleParcel();
    
}

