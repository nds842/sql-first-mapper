package com.github.nds842.sqlfirst.springsample;

import com.github.nds842.sqlfirst.springsample.common.SenderNameItem;
import com.github.nds842.sqlfirst.springsample.common.StartDateItem;
import com.github.nds842.sqlfirst.springsample.model.Letter;
import com.github.nds842.sqlfirst.springsample.model.Parcel;
import com.github.nds842.sqlfirst.springsample.model.TrackCode;
import com.github.nds842.sqlfirst.springsample.repository.LetterRepository;
import com.github.nds842.sqlfirst.springsample.repository.LetterRepositoryCustom;
import com.github.nds842.sqlfirst.springsample.repository.LetterRepositoryImpl;
import com.github.nds842.sqlfirst.springsample.repository.ParcelRepository;
import com.github.nds842.sqlfirst.springsample.repository.ParcelRepositoryApc;
import com.github.nds842.sqlfirst.springsample.repository.ParcelRepositoryCustom;
import com.github.nds842.sqlfirst.springsample.repository.ParcelRepositoryImpl;
import com.github.nds842.sqlfirst.springsample.repository.TrackCodeRepository;
import com.github.nds842.sqlfirst.springsample.repository.TrackCodeRepositoryCustom;
import com.github.nds842.sqlfirst.springsample.repository.TrackCodeRepositoryImpl;
import com.github.nds842.sqlfirst.springsample.repository.TrackCodeRepositoryParent;
import com.github.nds842.sqlfirst.springsample.repository.dto.CheckTrackCodeReq;
import com.github.nds842.sqlfirst.springsample.repository.dto.CheckTrackCodeRes;
import com.github.nds842.sqlfirst.springsample.repository.dto.FindSampleLetterReq;
import com.github.nds842.sqlfirst.springsample.repository.dto.FindSampleLetterRes;
import com.github.nds842.sqlfirst.springsample.repository.dto.FindSampleParcelReq;
import com.github.nds842.sqlfirst.springsample.repository.dto.FindSampleParcelRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class SampleSpringTest extends BaseTest {
    
    @Autowired
    private LetterRepository letterRepository;
    
    @Autowired
    private TrackCodeRepository trackCodeRepository;
    
    @Autowired
    private ParcelRepository parcelRepository;
    
    @Test
    public void testFindSampleLetter() {
        Letter letter = prepareLetter();
        
        Iterable<Letter> allLetters = letterRepository.findAll();
        
        Iterator<Letter> letterIterator = allLetters.iterator();
        Assert.assertTrue(letterIterator.hasNext());
        Assert.assertEquals(letterIterator.next(), letter);
        Assert.assertFalse(letterIterator.hasNext());
        
        
        FindSampleLetterReq req = new FindSampleLetterReq();
        req.setSenderName("some name");
        List<FindSampleLetterRes> resList = letterRepository.findSampleLetter(req);
        
        Assert.assertEquals(resList.size(), 1);
        FindSampleLetterRes findSampleLetterRes = resList.get(0);
        Assert.assertEquals(findSampleLetterRes.getId(), letter.getId());
        Assert.assertTrue(findSampleLetterRes instanceof SenderNameItem);
    }
    
    @Test
    public void testTrackCode(){
        TrackCode trackCode = new TrackCode();
        trackCode.setTrackCode("123");
        Letter letter = prepareLetter();
        trackCode.setPostage(letter);
        trackCodeRepository.save(trackCode);
    
        Assert.assertEquals(trackCodeRepository.countSampleLetters(), "Sample letter count: 1");
    
        CheckTrackCodeReq req = new CheckTrackCodeReq();
        req.setPostageId(letter.getId());
        List<CheckTrackCodeRes> resList = trackCodeRepository.checkTrackCode(req);
    
        Assert.assertEquals(resList.size(), 1);
        CheckTrackCodeRes checkTrackCodeRes = resList.get(0);
        Assert.assertEquals(checkTrackCodeRes.getId(), trackCode.getId());
    }
    
    @Test
    public void testParcel(){
        Parcel parcel = prepareParcel();
    
        Iterable<Parcel> allParcels = parcelRepository.findAll();
    
        Iterator<Parcel> parcelIterator = allParcels.iterator();
        Assert.assertTrue(parcelIterator.hasNext());
        Assert.assertEquals(parcelIterator.next(), parcel);
        Assert.assertFalse(parcelIterator.hasNext());
    
        List<Parcel> entityList = parcelRepository.getParcelBySenderName("some name");
    
        Assert.assertEquals(entityList.size(), 1);
        Parcel foundParcel = entityList.get(0);
        Assert.assertEquals(foundParcel, parcel);
    
    
        FindSampleParcelReq req = new FindSampleParcelReq();
        req.setSender("some name");
        List<FindSampleParcelRes> resList = parcelRepository.findSampleParcel(req);
        Assert.assertEquals(resList.size(), 1);
        FindSampleParcelRes findSampleParcelRes = resList.get(0);
        Assert.assertEquals(findSampleParcelRes.getId(), parcel.getId());
        Assert.assertTrue(findSampleParcelRes instanceof SenderNameItem);
    
    }
    
    @Test
    public void checkImplements() {
        //Generated classes implement custom interfaces
        Assert.assertTrue(SenderNameItem.class.isAssignableFrom(FindSampleLetterRes.class));
        Assert.assertTrue(SenderNameItem.class.isAssignableFrom(FindSampleLetterReq.class));
        Assert.assertTrue(StartDateItem.class.isAssignableFrom(FindSampleLetterReq.class));
        
        //No @SqlSource annotation
        Assert.assertTrue(LetterRepositoryCustom.class.isAssignableFrom(LetterRepositoryImpl.class));
        
        //@SqlSource annotation with custom targetClassName
        Assert.assertTrue(ParcelRepositoryCustom.class.isAssignableFrom(ParcelRepositoryApc.class));
        Assert.assertTrue(ParcelRepositoryApc.class.isAssignableFrom(ParcelRepositoryImpl.class));
        
        //@SqlSource annotation with custom baseDaoClassName
        Assert.assertTrue(TrackCodeRepositoryCustom.class.isAssignableFrom(TrackCodeRepositoryImpl.class));
        Assert.assertTrue(TrackCodeRepositoryParent.class.isAssignableFrom(TrackCodeRepositoryImpl.class));
    }
    
    private Letter prepareLetter() {
        Letter letter = new Letter();
        letter.setHeight(1L);
        letter.setWeight(2L);
        letter.setWidth(3L);
        letter.setDateSend(new Date());
        letter.setSenderName("some name");
        
        letterRepository.save(letter);
        return letter;
    }
    
    private Parcel prepareParcel() {
        Parcel parcel = new Parcel();
        parcel.setHeight(1L);
        parcel.setWeight(2L);
        parcel.setWidth(3L);
        parcel.setDateSend(new Date());
        parcel.setSenderName("some name");
    
        parcelRepository.save(parcel);
        return parcel;
    }
    
}
