package com.github.nds842.sqlfirst.springsample;

import com.github.nds842.sqlfirst.springsample.model.Letter;
import com.github.nds842.sqlfirst.springsample.repository.FindSampleLetterReq;
import com.github.nds842.sqlfirst.springsample.repository.FindSampleLetterRes;
import com.github.nds842.sqlfirst.springsample.repository.LetterRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
public class LetterSampleSpringTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Autowired
    private LetterRepository letterRepository;
    
    @Test
    public void test() {
        Letter letter = new Letter();
        letter.setHeight(1L);
        letter.setWeight(2L);
        letter.setWidth(3L);
        letter.setDateSend(new Date());
        letter.setSenderName("some name");
        
        letterRepository.save(letter);
        letterRepository.findAll().forEach(x-> System.out.println("x = " + x));
    
        FindSampleLetterReq req = new FindSampleLetterReq();
        req.setSender("some name");
        List<FindSampleLetterRes> resList = letterRepository.findSampleLetter(req);
    
        System.out.println("resList = " + resList);
        
    }
    
}
