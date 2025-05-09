package com.redmopag.documentmanagment.textservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.UUID;

@Document(indexName = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentText {
    @Id
    private String id = UUID.randomUUID().toString();

    @Field(type = FieldType.Long, index = false)
    private Long documentId;

    @Field(type = FieldType.Text, index = false)
    private String hocrContent;

    @Field(type = FieldType.Text)
    private String text;
}
