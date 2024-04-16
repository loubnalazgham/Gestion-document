package com.example.api_doc.Requests;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Attribute {
    String key;
    String value;
}
