package org.xapps.service.commentsservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.xapps.service.commentsservice.dtos.CommentCreateRequest;
import org.xapps.service.commentsservice.dtos.CommentEditRequest;
import org.xapps.service.commentsservice.dtos.CommentResponse;
import org.xapps.service.commentsservice.dtos.UserAuthenticated;
import org.xapps.service.commentsservice.services.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/comments")
public class CommentsController {

    private Logger logger = LoggerFactory.getLogger(CommentsController.class);
    private CommentService commentService;

    @Autowired
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        ResponseEntity<List<CommentResponse>> response = null;
        try {
            List<CommentResponse> comments = commentService.getAllComments();
            response = new ResponseEntity<>(comments, HttpStatus.OK);
        } catch(Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin') or isAuthenticated() and principal.id == #id")
    public ResponseEntity<List<CommentResponse>> getCommentsByUser(@PathVariable("id") Long id) {
        ResponseEntity<List<CommentResponse>> response = null;
        try {
            List<CommentResponse> comments = commentService.getCommentsByUser(id);
            response = new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(path = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentResponse>> getCommentsByProduct(@PathVariable("id") Long id) {
        ResponseEntity<List<CommentResponse>> response = null;
        try {
            List<CommentResponse> comments = commentService.getCommentsByProduct(id);
            response = new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(path = "/user/{userId}/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin') or isAuthenticated() and principal.id == #userId")
    public ResponseEntity<List<CommentResponse>> getCommentByUserandProduct(@PathVariable("userId") Long userId, @PathVariable("productId") Long productId) {
        ResponseEntity<List<CommentResponse>> response = null;
        try {
            List<CommentResponse> comments = commentService.getCommentsByUserAndProduct(userId, productId);
            response = new ResponseEntity<>(comments, HttpStatus.OK);
        } catch(Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin') or isAuthenticated() and principal.id == #commentCreateRequest.userId")
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentCreateRequest commentCreateRequest) {
        ResponseEntity<CommentResponse> response = null;
        try {
            CommentResponse comment = commentService.createComment(commentCreateRequest);
            response = new ResponseEntity<>(comment, HttpStatus.CREATED);
        } catch(Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> editComment(@PathVariable("id") Long id, @Valid @RequestBody CommentEditRequest commentEditRequest) {
        ResponseEntity<CommentResponse> response = null;
        try {
            UserAuthenticated user = (UserAuthenticated) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user.hasRole("admin") || commentService.isUserOwner(user.getId(), id)) {
                CommentResponse comment = commentService.editComment(id, commentEditRequest);
                if(comment != null) {
                    response = new ResponseEntity<>(comment, HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                response = new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        ResponseEntity<Void> response = null;
        try {
            UserAuthenticated user = (UserAuthenticated) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user.hasRole("admin") || commentService.isUserOwner(user.getId(), id)) {
                boolean success = commentService.deleteComment(id);
                if (success) {
                    response = new ResponseEntity<>(HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                response = new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            logger.error("Exception captured");
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
