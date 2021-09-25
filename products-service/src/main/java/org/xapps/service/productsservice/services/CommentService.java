package org.xapps.service.productsservice.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.xapps.service.productsservice.dtos.CommentResponse;

import java.util.List;

@FeignClient("commentsservice")
public interface CommentService {

    @GetMapping(path = "/comments/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentResponse> getCommentsByProduct(@PathVariable("id") Long id);

}
