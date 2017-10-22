package com.github.nds842.sqlfirst.springsample.repository;

import com.github.nds842.sqlfirst.springsample.model.Letter;
import org.springframework.data.repository.CrudRepository;

public interface TrackCodeRepository extends TrackCodeRepositoryCustom, CrudRepository<Letter, Long> {
}
