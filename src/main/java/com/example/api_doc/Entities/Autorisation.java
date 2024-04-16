package com.example.api_doc.Entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Autorisation {

    private Integer id;
    private Document document;
    private User user;
    private String typeAutorisation;
}
