package com.test.feedaggregator.service;

import com.test.feedaggregator.entity.FeedEntry;

import java.util.List;

public interface FeedEntriesService {

    List<FeedEntry> getFeedEntries(String titleFilter);

}
