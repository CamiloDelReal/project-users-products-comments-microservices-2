package org.xapps.service.commentsservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {
    private Long userId;
    private Long productId;
    private String text;

    public CommentCreateRequest() {
    }

    public CommentCreateRequest(Long userId, Long productId, String text) {
        this.userId = userId;
        this.productId = productId;
        this.text = text;
    }
}
