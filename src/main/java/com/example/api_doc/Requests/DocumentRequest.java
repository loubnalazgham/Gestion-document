package com.example.api_doc.Requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class DocumentRequest {
//    private Integer uuid;
//    private String nomDocument;
//    private String typeDocument;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
//    private LocalDateTime dateCreation;
    private MetaData metadataSup;
}
