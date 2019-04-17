package com.test.feedaggregator.repository;

import com.test.feedaggregator.entity.FeedEntry;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FeedEntriesRepository extends JpaRepository<FeedEntry, Long> {

    @Transactional(readOnly = true)
    List<FeedEntry> findByTitleIgnoreCaseContaining(String title, Sort sort);

}
