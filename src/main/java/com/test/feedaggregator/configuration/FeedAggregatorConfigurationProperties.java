package com.test.feedaggregator.configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;
import java.time.Duration;
import java.util.List;

@Setter
@Getter
@ConfigurationProperties("feed-aggregator")
public class FeedAggregatorConfigurationProperties {

    private PollerConfig pollerConfig;
    private List<URL> sources;

    @Setter
    @Getter
    public static class PollerConfig {

        private Duration rate;
        private Long maxMessagesPerPoll;

    }
}
