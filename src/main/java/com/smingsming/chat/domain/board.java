package com.smingsming.chat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "board")
@Data
@NoArgsConstructor
public class board {
    @Id
    private String id;
}
