package com.github.nds842.sqlfirst.parcelsample;

import com.github.nds842.sqlfirst.initdbsample.InitDbSample;
import com.github.nds842.sqlfirst.initdbsample.InitDbSampleDao;
import com.github.nds842.sqlfirst.interfaces.SenderNameItem;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleLetterReq;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleLetterRes;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleParcelReq;
import com.github.nds842.sqlfirst.parcelsample.dto.FindSampleParcelRes;
import com.github.nds842.sqlfirst.parcelsample.dto.InsertSampleLetterReq;
import com.github.nds842.sqlfirst.parcelsample.dto.InsertSampleParcelReq;
import com.github.nds842.sqlfirst.tracksample.TrackCodeSample;
import com.github.nds842.sqlfirst.tracksample.TrackCodeSampleDao;
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

    private static final long WEIGHT = 11L;
    private static final long WIDTH = 4L;
    private static final long HEIGHT = 10L;
    private static final String SOMENAME = "somename";

    private Connection conn;

    @BeforeTest
    private void beforeTest() throws Exception {
        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection("jdbc:h2:mem:~/test", "sa", "");
        InitDbSampleDao.getInstance().createTables(conn);
    }

    @AfterTest
    private void afterTest() throws Exception {
        conn.rollback();
        conn.close();
    }

    @Test
    public void testLetter() throws SQLException {
        LetterSampleDao dao = LetterSampleDao.getInstance();

        dao.deleteSampleLetter(conn);

        InsertSampleLetterReq insertReq = new InsertSampleLetterReq();
        insertReq.setHeight(HEIGHT);
        insertReq.withWidth(WIDTH).withWeight(WEIGHT).withSendDate(new Date()).withSenderName(SOMENAME);
        dao.insertSampleLetter(insertReq, conn);

        FindSampleLetterReq findReq = new FindSampleLetterReq().withSender(SOMENAME).withWidth(WIDTH).withHeight(HEIGHT);
        List<FindSampleLetterRes> foundList = dao.findSampleLetter(findReq, conn);

        Assert.assertEquals(foundList.size(), 1);
        Assert.assertEquals(foundList.get(0).getWeight(), insertReq.getWeight());
    }


    @Test
    public void testParcel() throws SQLException {
        ParcelSampleDao dao = ParcelSampleDao.getInstance();

        LetterSampleDao.getInstance().deleteSampleLetter(conn);

        InsertSampleParcelReq insertReq = new InsertSampleParcelReq();
        insertReq.withSenderName(SOMENAME).withWidth(WIDTH).withHeight(HEIGHT).withWeight(WEIGHT).withSendDate(new Date());
        dao.insertSampleParcel(insertReq, conn);

        List<FindSampleParcelRes> foundList = dao.findSampleParcel(new FindSampleParcelReq().withWeight(WEIGHT).withSender(SOMENAME), conn);

        Assert.assertEquals(foundList.size(), 1);
        Assert.assertEquals(foundList.get(0).toMap(), insertReq.toMap(), foundList.toString());

        dao.deleteSampleParcel(conn);

        Assert.assertEquals(dao.findSampleParcel(new FindSampleParcelReq().withWeight(WEIGHT), conn).size(), 0);
    }


    @Test
    public void testDtoImplements() {
        Assert.assertTrue(SenderNameItem.class.isAssignableFrom(FindSampleParcelRes.class));
    }

    @Test
    public void testDaoImplements() {
        Assert.assertFalse(ParcelSample.class.isAssignableFrom(ParcelSampleDao.class));
        Assert.assertTrue(LetterSample.class.isAssignableFrom(LetterSampleDao.class));
        Assert.assertTrue(InitDbSample.class.isAssignableFrom(InitDbSampleDao.class));
        Assert.assertTrue(TrackCodeSample.class.isAssignableFrom(TrackCodeSampleDao.class));
    }
}
