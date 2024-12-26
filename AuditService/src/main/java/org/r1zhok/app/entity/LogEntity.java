package org.r1zhok.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Document(indexName = "audit_logs")
public class LogEntity {

    @Id
    private UUID id;

    @Field(type = FieldType.Text)
    private String service;

    @Field(type = FieldType.Text)
    private String action;

    @Field(type = FieldType.Object)
    private Map<String, Object> details;

    @Field(type = FieldType.Text, name = "performed_by")
    private String performedBy;

    @Field(type = FieldType.Date)
    private LocalDateTime timestamp;
}