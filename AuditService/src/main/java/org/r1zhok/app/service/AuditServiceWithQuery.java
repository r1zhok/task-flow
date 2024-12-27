package org.r1zhok.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.r1zhok.app.entity.LogEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* "query": "Oleksii34-performed_by login-action"
* example input
* */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceWithQuery {

    private static final String LOG_INDEX = "audit_logs";

    private final ElasticsearchOperations elasticsearchOperations;

    public List<LogEntity> processSearch(String query) {
        String[] queryParts = query.split("\\s+");
        List<String> listParts = new ArrayList<>(Arrays.asList(queryParts));
        listParts.remove(0); // removeFirst() and removeLast() javac throw error however I use java 21
        listParts.remove(0);
        listParts.remove(listParts.size() - 1);
        queryParts = listParts.toArray(new String[0]);

        for (String part : queryParts) {
            log.info(part);
        }

        SearchHits<LogEntity> logHits =
                elasticsearchOperations.search(configSearchQuery(queryParts), LogEntity.class, IndexCoordinates.of(LOG_INDEX));

        log.info(logHits.toString());

        List<LogEntity> logMatches = new ArrayList<>();
        logHits.forEach(searchHit -> logMatches.add(searchHit.getContent()));

        return logMatches;
    }

    public List<String> fetchSuggestions(String query) {
        String[] queryParts = query.split("\\s+");

        SearchHits<LogEntity> searchSuggestions =
                elasticsearchOperations.search(configSearchQuery(queryParts), LogEntity.class, IndexCoordinates.of(LOG_INDEX));

        List<String> suggestions = new ArrayList<>();
        searchSuggestions.getSearchHits().forEach(searchHit -> suggestions.add(searchHit.getContent().getService()));

        return suggestions;
    }

    private Query configSearchQuery(String[] queryParts) {
        NativeQueryBuilder queryBuilder = new NativeQueryBuilder();
        
        for (String part : queryParts) {
            if (part.contains("service")) {
                queryBuilder.withQuery(q ->
                        q.match(m -> m.field("service").query(part).fuzziness("AUTO"))
                );
            } else if (part.contains("action")) {
                queryBuilder.withQuery(q ->
                        q.match(m -> m.field("action").query(part.split("-")[0]).fuzziness("AUTO"))
                );
            } else if (part.contains("performed_by")) {
                queryBuilder.withQuery(q ->
                        q.match(m -> m.field("performed_by").query(part.split("-")[0]).fuzziness("AUTO"))
                );
            } else if (part.contains("timestamp")) {
                queryBuilder.withQuery(q ->
                        q.match(m -> m.field("timestamp").query(part.split("-")[0]).fuzziness("AUTO"))
                );
            }
        }

        return queryBuilder
                .withPageable(PageRequest.of(0, 5))
                .build();
    }
}