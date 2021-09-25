package org.xapps.service.commentsservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentEditRequest {
    private String text;

    public CommentEditRequest() {
    }

    public CommentEditRequest(Long userId, String text) {
        this.text = text;
    }
}
