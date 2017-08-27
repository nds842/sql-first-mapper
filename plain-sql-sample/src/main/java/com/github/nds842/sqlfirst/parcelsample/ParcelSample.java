package com.github.nds842.sqlfirst.parcelsample;


import com.github.nds842.sqlfirst.apc.SqlSource;
import com.github.nds842.sqlfirst.apc.SqlSourceFile;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleParcelReq;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleParcelRes;
import com.github.nds842.sqlfirst.parcelsample.dto.InsertSampleParcelReq;

import java.sql.Connection;
import java.util.List;

@SqlSourceFile
public interface ParcelSample {

    /**
     * SELECT
     * parcel.height          height__l,
     * parcel.width           width__l,
     * parcel.date_send       send_date__d,
     * parcel.weight          weight__l,
     * parcel.sender_name     sender_name__s
     * FROM parcel WHERE
     *   sender_name = :sender__s
     *   #if(${width__l})       AND parcel.width  = :width__l       #end
     *   #if(${weight__l})      AND parcel.weight = :weight__l      #end
     *   #if(${height__l})      AND parcel.height = :height__l      #end
     *   #if(${start_date__d})  AND parcel.date_send  between :start_date__d and :end_date__d #end
     */
    @SqlSource
    List<FindSampleParcelRes> findSampleParcel(FindSampleParcelReq req, Connection conn);

    /**
     * INSERT into parcel (height,width,date_send,weight,sender_name) VALUES (
     * :height__l,:width__l,:send_date__d,:weight__l,:sender_name__s)
     */
    @SqlSource
    void insertSampleParcel(InsertSampleParcelReq req, Connection conn);


    /**
     * DELETE FROM parcel
     */
    @SqlSource
    void deleteSampleParcel(Connection conn);
}