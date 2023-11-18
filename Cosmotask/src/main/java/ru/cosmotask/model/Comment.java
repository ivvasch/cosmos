package ru.cosmotask.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "t_comment")
public class Comment {
    private String id;
    private String text;
}
