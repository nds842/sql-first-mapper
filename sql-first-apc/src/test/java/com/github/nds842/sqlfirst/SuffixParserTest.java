package com.github.nds842.sqlfirst;

import com.github.nds842.sqlfirst.base.QueryDesc;
import com.github.nds842.sqlfirst.parser.SuffixParser;
import org.testng.Assert;
import org.testng.annotations.Test;


public class SuffixParserTest {


    @Test
    public void test() {

        String queryText = "  SELECT\n" +
                " letter.height          height__l,\n" +
                " letter.width           width__l,\n" +
                " letter.date_send       send_date__d,\n" +
                " letter.weight          weight__l\n" +
                " FROM letter\n" +
                " WHERE " +
                " #if(${width__l}) letter.width = :width__l #end\n" +
                " #if(${height__l}) letter.height = :height__l #end\n" +
                " #if(${start_date__d})  letter.date_send  between :start_date__d and :end_date__d #end";

        QueryDesc qDesc = new SuffixParser().parse(
                queryText
        );

        Assert.assertEquals(
                qDesc.getRequestParamList().toString(),
                "[" +
                            "[Long, width__l, width], " +
                            "[Long, height__l, height], " +
                            "[Date, start_date__d, startDate], " +
                            "[Date, end_date__d, endDate]" +
                        "]");
        Assert.assertEquals(
                qDesc.getResponseParamList().toString(),
                "[" +
                            "[Long, height__l, height], " +
                            "[Long, width__l, width], " +
                            "[Date, send_date__d, sendDate], " +
                            "[Long, weight__l, weight]" +
                        "]");
    }

}
