package com.test.feedaggregator.entity;

import com.rometools.rome.feed.synd.SyndEntry;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor()
public class FeedEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String title;
    @Lob
    private String uri;

    private Date publishedDate;

    public static FeedEntry fromSyndEntry(SyndEntry entry) {
        return builder()
                .title(entry.getTitle())
                .uri(entry.getUri())
                .publishedDate(entry.getPublishedDate())
                .build();
    }

}
