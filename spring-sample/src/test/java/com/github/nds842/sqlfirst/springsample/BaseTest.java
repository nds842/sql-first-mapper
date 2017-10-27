package com.github.nds842.sqlfirst.springsample;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

@SpringBootTest

public class BaseTest extends AbstractTransactionalTestNGSpringContextTests {
    
    
    protected <T> T prepareTestDto(Class<T> reqClass) {
        try {
            return reqClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
