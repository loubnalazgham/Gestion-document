package com.example.api_doc.Requests;


import com.example.api_doc.Entities.Document;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MetaData {
    List<Attribute> attributes = new ArrayList<>();

}
