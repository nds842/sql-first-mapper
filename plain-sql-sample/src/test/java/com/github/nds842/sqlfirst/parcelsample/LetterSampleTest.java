package com.github.nds842.sqlfirst.parcelsample;

import com.github.nds842.sqlfirst.initdbsample.InitDbSampleDao;
import com.github.nds842.sqlfirst.interfaces.SenderNameItem;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleLetterReq;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleLetterRes;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleParcelReq;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleParcelRes;
import com.github.nds842.sqlfirst.parcelsample.dto.InsertSampleLetterReq;
import com.github.nds842.sqlfirst.parcelsample.dto.InsertSampleParcelReq;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class LetterSampleTest {

    private Connection conn;

    private String SOMENAME = "somename";

    @BeforeTest
    private void beforeTest() throws Exception {
        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection("jdbc:h2:mem:~/test", "sa", "");
        InitDbSampleDao.getIntance().createTables(conn);
    }

    @AfterTest
    private void afterTest() throws Exception {
        conn.rollback();
        conn.close();
    }

    @Test
    public void testLetter() throws SQLException {
        LetterSampleDao dao = LetterSampleDao.getIntance();

        dao.deleteSampleLetter(conn);

        InsertSampleLetterReq insertReq = new InsertSampleLetterReq();
        insertReq.setHeight(10L);
        insertReq.withWidth(4L).withWeight(11L).withSendDate(new Date()).withSenderName(SOMENAME);
        dao.insertSampleLetter(insertReq, conn);

        FindSampleLetterReq findReq = new FindSampleLetterReq().withSender(SOMENAME).withWidth(4L).withHeight(10L);
        List<FindSampleLetterRes> foundList = dao.findSampleLetter(findReq, conn);

        Assert.assertEquals(foundList.size(), 1);
        Assert.assertEquals(foundList.get(0).getWeight(), insertReq.getWeight());
    }


    @Test
    public void testParcel() throws SQLException {

        Assert.assertTrue(SenderNameItem.class.isAssignableFrom(FindSampleParcelRes.class));
        ParcelSampleDao dao = ParcelSampleDao.getIntance();

        LetterSampleDao.getIntance().deleteSampleLetter(conn);

        InsertSampleParcelReq insertReq = new InsertSampleParcelReq();
        insertReq.withSenderName(SOMENAME).withWidth(4L).withHeight(10L).withWeight(11L).withSendDate(new Date());
        dao.insertSampleParcel(insertReq, conn);

        List<FindSampleParcelRes> foundList = dao.findSampleParcel(new FindSampleParcelReq().withWeight(11L).withSender(SOMENAME), conn);

        Assert.assertEquals(foundList.size(), 1);
        Assert.assertEquals(foundList.get(0).toMap(), insertReq.toMap(), foundList.toString());

        dao.deleteSampleParcel(conn);

        Assert.assertEquals(dao.findSampleParcel(new FindSampleParcelReq().withWeight(11L), conn).size(), 0);
    }
}
