package com.github.nds842.sqlfirst.springsample.repository;

import com.github.nds842.sqlfirst.springsample.model.TrackCode;
import org.springframework.data.repository.CrudRepository;

public interface TrackCodeRepository extends TrackCodeRepositoryCustom, CrudRepository<TrackCode, Long> {
}
