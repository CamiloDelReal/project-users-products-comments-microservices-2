package org.xapps.service.commentsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.xapps.service.commentsservice.entities.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserId(Long userId);

    List<Comment> findByProductId(Long productId);

    List<Comment> findByUserIdAndProductId(Long userId, Long productId);

    Optional<Comment> findByIdAndUserId(Long id, Long userId);

}
