package com.redmopag.documentmanagment.documentservice.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentText {
    @Id
    private Long documentId;

    @Field(type = FieldType.Text)
    private String text;

    @Field(type = FieldType.Text)
    private String hocrContent;
}
