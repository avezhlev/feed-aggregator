# RSS feed aggregator
Aggregates RSS feed entries and displays them via simple Web UI.
###
Feed entries list is available at http://localhost:8080/.  

The default pre-configured RSS feeds are https://www.sciencedaily.com/rss/top/science.xml and http://feeds.reuters.com/reuters/technologyNews.  
Source feeds list is overridable by providing a comma-separated list using the `feed-aggregator.sources` execution argument (e.g. `--feed-aggregator.sources=https://some.domain/rss,http://feeds.another.domain/news`).
