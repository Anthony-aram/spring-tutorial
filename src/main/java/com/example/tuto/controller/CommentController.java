package com.example.tuto.controller;

import com.example.tuto.dto.CommentDto;
import com.example.tuto.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Create a comment associated to a post
     * @param postId Associated post
     * @param commentDto Comment to add to the post
     * @return Created comment
     */
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId, @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    /**
     * Get all comments from a post
     * @param postId Post id
     * @return Post comments
     */
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(name = "postId") Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    /**
     * Get a comment from a post
     * @param postId Post id
     * @param commentId Comment id
     * @return A comment
     */
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(postId, commentId));
    }

    /**
     * Update a comment from a post
     * @param postId Post id
     * @param commentId Comment id
     * @param commentDto Comment to update
     * @return Updated comment
     */
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateComment(postId, commentId, commentDto));
    }

    /**
     * Delete a comment from a post
     * @param postId Post id
     * @param commentId Comment id
     * @return A String
     */
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String>  deleteComment(@PathVariable(name = "postId") long postId, @PathVariable(name = "commentId") long commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
