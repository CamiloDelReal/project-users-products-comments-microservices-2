package org.xapps.service.commentsservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String text;
}
