package org.xapps.service.productsservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private Long productId;
    private Long userId;
    private String text;

    public CommentResponse() {
    }

    public CommentResponse(Long id, Long productId, Long userId, String text) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.text = text;
    }
}
