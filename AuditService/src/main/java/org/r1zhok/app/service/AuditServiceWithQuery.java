package org.r1zhok.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.r1zhok.app.entity.LogEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceWithQuery {

    private static final String LOG_INDEX = "audit_logs";

    private final ElasticsearchOperations elasticsearchOperations;

    public List<LogEntity> processSearch(String query) {
        String[] queryParts = query.split("\\s+");

        Query searchQuery = configSearchQuery(queryParts);

        SearchHits<LogEntity> logHits =
                elasticsearchOperations.search(searchQuery, LogEntity.class, IndexCoordinates.of(LOG_INDEX));

        List<LogEntity> logMatches = new ArrayList<>();
        logHits.forEach(searchHit -> logMatches.add(searchHit.getContent()));

        return logMatches;
    }

    public List<String> fetchSuggestions(String query) {
        String[] queryParts = query.split("\\s+");

        Query searchQuery = configSearchQuery(queryParts);

        SearchHits<LogEntity> searchSuggestions =
                elasticsearchOperations.search(searchQuery, LogEntity.class, IndexCoordinates.of(LOG_INDEX));

        List<String> suggestions = new ArrayList<>();
        searchSuggestions.getSearchHits().forEach(searchHit -> suggestions.add(searchHit.getContent().getService()));

        return suggestions;
    }

    private Query configSearchQuery(String[] queryParts) {
        return new NativeQueryBuilder()
                .withQuery(q ->
                        q.match(m -> m.field("service").query(queryParts[0] == null ? null : queryParts[0])
                                .fuzziness(String.valueOf(Fuzziness.AUTO)))
                )
                .withQuery(q ->
                        q.match(m -> m.field("action").query(queryParts[1] == null ? null : queryParts[1])
                                .fuzziness(String.valueOf(Fuzziness.AUTO)))
                )
                .withQuery(q ->
                        q.match(m -> m.field("performed_by").query(queryParts[2] == null ? null : queryParts[2])
                                .fuzziness(String.valueOf(Fuzziness.AUTO)))
                )
                .withQuery(q ->
                        q.match(m -> m.field("details").query(queryParts[3] == null ? null : queryParts[3])
                                .fuzziness(String.valueOf(Fuzziness.AUTO)))

                )
                .withPageable(PageRequest.of(0, 5))
                .build();
    }
}
