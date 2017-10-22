package com.github.nds842.sqlfirst.springsample.repository;


import com.github.nds842.sqlfirst.springsample.model.Parcel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParcelRepository extends ParcelRepositoryCustom, CrudRepository<Parcel, Long> {
    
    List<Parcel> getParcelBySenderName(String senderName);
    
}
