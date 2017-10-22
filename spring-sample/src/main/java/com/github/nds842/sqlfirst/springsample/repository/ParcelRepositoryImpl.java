package com.github.nds842.sqlfirst.springsample.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;

@Repository
public class ParcelRepositoryImpl extends ParcelRepositoryApc implements ParcelRepositoryCustom {
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    public void deleteAllBySenderName(String senderName) {
        namedParameterJdbcTemplate.update(
                "delete from parcel where sender_name = :sender_name",
                Collections.singletonMap("sender_name", "senderName")
        );
    }
}
