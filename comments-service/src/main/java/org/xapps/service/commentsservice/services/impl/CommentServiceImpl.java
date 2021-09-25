package org.xapps.service.commentsservice.services.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xapps.service.commentsservice.dtos.CommentCreateRequest;
import org.xapps.service.commentsservice.dtos.CommentEditRequest;
import org.xapps.service.commentsservice.dtos.CommentResponse;
import org.xapps.service.commentsservice.entities.Comment;
import org.xapps.service.commentsservice.repositories.CommentRepository;
import org.xapps.service.commentsservice.services.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private ModelMapper modelMapper;
    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(ModelMapper modelMapper, CommentRepository commentRepository) {
        this.modelMapper = modelMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public boolean isUserOwner(Long userId, Long commentId) {
        return (commentRepository.findByIdAndUserId(commentId, userId).orElse(null) != null);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        List<CommentResponse> response = null;
        List<Comment> comments = commentRepository.findAll();
        if(comments != null && !comments.isEmpty()) {
            response = comments.stream().map(c -> modelMapper.map(c, CommentResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public CommentResponse getCommentById(Long id) {
        CommentResponse response = null;
        Comment comment = commentRepository.findById(id).orElse(null);
        if(comment != null) {
            response = modelMapper.map(comment, CommentResponse.class);
        }
        return response;
    }

    @Override
    public List<CommentResponse> getCommentsByUser(Long userId) {
        List<CommentResponse> response = null;
        List<Comment> comments = commentRepository.findByUserId(userId);
        if(comments != null && !comments.isEmpty()) {
            response = comments.stream().map(c -> modelMapper.map(c, CommentResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public List<CommentResponse> getCommentsByProduct(Long productId) {
        List<CommentResponse> response = null;
        List<Comment> comments = commentRepository.findByProductId(productId);
        if(comments != null && !comments.isEmpty()) {
            response = comments.stream().map(c -> modelMapper.map(c, CommentResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public List<CommentResponse> getCommentsByUserAndProduct(Long userId, Long productId) {
        List<CommentResponse> response = null;
        List<Comment> comments = commentRepository.findByUserIdAndProductId(userId, productId);
        if(comments != null && !comments.isEmpty()) {
            response = comments.stream().map(c -> modelMapper.map(c, CommentResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public CommentResponse createComment(CommentCreateRequest commentCreateRequest) {
        Comment comment = modelMapper.map(commentCreateRequest, Comment.class);
        commentRepository.save(comment);
        CommentResponse response = modelMapper.map(comment, CommentResponse.class);
        return response;
    }

    @Override
    public CommentResponse editComment(Long id, CommentEditRequest commentEditRequest) {
        CommentResponse response = null;
        Comment comment = commentRepository.findById(id).orElse(null);
        if(comment != null) {
            comment.setText(commentEditRequest.getText());
            commentRepository.save(comment);
            response = modelMapper.map(comment, CommentResponse.class);
        }
        return response;
    }

    @Override
    public boolean deleteComment(Long id) {
        boolean success = false;
        Comment comment = commentRepository.findById(id).orElse(null);
        if(comment != null) {
            commentRepository.delete(comment);
            success = true;
        }
        return success;
    }

}
