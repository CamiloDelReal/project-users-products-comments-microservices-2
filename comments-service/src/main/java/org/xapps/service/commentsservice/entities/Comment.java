package org.xapps.service.commentsservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "text")
    private String text;

    public Comment() {
    }

    public Comment(Long userId, Long productId, String text) {
        this.userId = userId;
        this.productId = productId;
        this.text = text;
    }
}
