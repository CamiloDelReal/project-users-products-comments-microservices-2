package org.xapps.service.commentsservice.services;

import org.xapps.service.commentsservice.dtos.CommentCreateRequest;
import org.xapps.service.commentsservice.dtos.CommentEditRequest;
import org.xapps.service.commentsservice.dtos.CommentResponse;

import java.util.List;

public interface CommentService {

    boolean isUserOwner(Long userId, Long commentId);

    List<CommentResponse> getAllComments();

    CommentResponse getCommentById(Long id);

    List<CommentResponse> getCommentsByUser(Long userId);

    List<CommentResponse> getCommentsByProduct(Long productId);

    List<CommentResponse> getCommentsByUserAndProduct(Long userId, Long productId);

    CommentResponse createComment(CommentCreateRequest commentCreateRequest);

    CommentResponse editComment(Long id, CommentEditRequest commentEditRequest);

    boolean deleteComment(Long id);

}
