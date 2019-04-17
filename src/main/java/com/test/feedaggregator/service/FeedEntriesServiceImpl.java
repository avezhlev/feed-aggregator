package com.test.feedaggregator.service;

import com.test.feedaggregator.entity.FeedEntry;
import com.test.feedaggregator.repository.FeedEntriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class FeedEntriesServiceImpl implements FeedEntriesService {

    private final FeedEntriesRepository repository;
    private final Sort sort = Sort.by(Sort.Order.desc("publishedDate"));

    @Autowired
    public FeedEntriesServiceImpl(FeedEntriesRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FeedEntry> getFeedEntries(String titleFilter) {
        try {
            return titleFilter == null || titleFilter.isBlank() ?
                    repository.findAll(sort) :
                    repository.findByTitleIgnoreCaseContaining(titleFilter, sort);
        }
        catch (RuntimeException e) {
            log.warn("Failed to retrieve feed entries", e);
            return Collections.emptyList();
        }
    }
}
