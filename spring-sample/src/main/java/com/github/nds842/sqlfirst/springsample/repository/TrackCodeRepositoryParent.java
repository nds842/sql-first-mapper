package com.github.nds842.sqlfirst.springsample.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCodeRepositoryParent {
    
    @Autowired
    private LetterRepository letterRepository;
    
    public String countSampleLetters() {
        return "Sample letter count: " + letterRepository.count();
    }
    
}
