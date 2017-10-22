package com.github.nds842.sqlfirst.springsample;

import com.github.nds842.sqlfirst.springsample.repository.TrackCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest
public class TrackCodeSampleSpringTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Autowired
    private TrackCodeRepository trackCodeRepository;
    
    @Test
    public void test() {
        Assert.assertEquals(trackCodeRepository.countSampleLetters(), "Sample letter count: 0");
    }
    
}
