package com.example.api_doc.Entities;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {

    private Integer id;
    private String nom;
    private String email;

}
