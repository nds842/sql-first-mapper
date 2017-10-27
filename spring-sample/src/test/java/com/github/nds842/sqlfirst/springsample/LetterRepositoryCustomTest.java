package com.github.nds842.sqlfirst.springsample;


import com.github.nds842.sqlfirst.apc.SqlSource;

public interface LetterRepositoryCustomTest {
    
    /**
     *  SELECT
     *  letter.id              id__l,
     *  letter.height          height__l,
     *  letter.width           width__l,
     *  letter.date_send       send_date__d,
     *  letter.weight          weight__l,
     *  letter.sender_name     sender_name__s
     *  FROM letter WHERE
     *    sender_name = :sender_name__s
     *    #if(${width__l})       AND letter.width  = :width__l       #end
     *    #if(${height__l})      AND letter.height = :height__l      #end
     *    #if(${start_date__d})  AND letter.date_send  between :start_date__d and :end_date__d #end
     */
    @SqlSource
    void xxxZZZ();
        
}
