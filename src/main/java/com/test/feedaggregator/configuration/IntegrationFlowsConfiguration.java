package com.test.feedaggregator.configuration;

import com.test.feedaggregator.entity.FeedEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.feed.dsl.Feed;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.SimpleMetadataStore;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.net.URL;

@Configuration
@EnableIntegration
@EnableConfigurationProperties(FeedAggregatorConfigurationProperties.class)
public class IntegrationFlowsConfiguration {

    private final IntegrationFlowContext integrationFlowContext;
    private final TransactionTemplate transactionTemplate;
    private final EntityManagerFactory entityManagerFactory;
    private final FeedAggregatorConfigurationProperties configuration;

    @Autowired
    public IntegrationFlowsConfiguration(IntegrationFlowContext integrationFlowContext,
                                         TransactionTemplate transactionTemplate, EntityManagerFactory entityManagerFactory,
                                         FeedAggregatorConfigurationProperties configuration) {
        this.integrationFlowContext = integrationFlowContext;
        this.transactionTemplate = transactionTemplate;
        this.entityManagerFactory = entityManagerFactory;
        this.configuration = configuration;
    }

    @PostConstruct
    private void registerFeedFlows() {
        for (URL source : configuration.getSources()) {
            integrationFlowContext.registration(
                    IntegrationFlows
                            .from(Feed.inboundAdapter(source, source.toString())
                                            .metadataStore(metadataStore()),
                                    s -> s.poller(Pollers.fixedRate(configuration.getPollerConfig().getRate())
                                            .maxMessagesPerPoll(configuration.getPollerConfig().getMaxMessagesPerPoll())))
                            .log(LoggingHandler.Level.DEBUG, message -> "Received SYND entry: " + message.getPayload())
                            .channel(feedEntriesChannel())
                            .get()
            ).register();
        }
    }

    @Bean
    IntegrationFlow outputToDatabaseFlow() {
        return IntegrationFlows
                .from(feedEntriesChannel())
                .transform(FeedEntry::fromSyndEntry)
                .log(LoggingHandler.Level.INFO, message -> "Persisting entry: " + message.getPayload())
                .handle(Jpa.outboundAdapter(entityManagerFactory)
                                .entityClass(FeedEntry.class)
                                .persistMode(PersistMode.PERSIST),
                        e -> e.transactional(transactionTemplate.getTransactionManager()))
                .get();
    }

    @Bean
    MetadataStore metadataStore() {
        return new SimpleMetadataStore();
    }

    @Bean
    MessageChannel feedEntriesChannel() {
        return new PublishSubscribeChannel();
    }
}
