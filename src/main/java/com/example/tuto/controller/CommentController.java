package com.example.tuto.controller;

import com.example.tuto.dto.CommentDto;
import com.example.tuto.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private final CommentService commentService;

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
    @Operation(summary = "Create a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public ResponseEntity<CommentDto> createComment(
            @Parameter(description = "id of the post to which you want to add a comment") @PathVariable(value = "postId") long postId,
            @Valid @RequestBody CommentDto commentDto
    ) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    /**
     * Get all comments from a post
     * @param postId Post id
     * @return Post comments
     */
    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "Get all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments successfully recovered",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public List<CommentDto> getCommentsByPostId(
            @Parameter(description = "id of the post containing the comments") @PathVariable(name = "postId") Long postId
    ) {
        return commentService.getCommentsByPostId(postId);
    }

    /**
     * Get a comment from a post
     * @param postId Post id
     * @param commentId Comment id
     * @return A comment
     */
    @GetMapping("/posts/{postId}/comments/{commentId}")
    @Operation(summary = "Get a comment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity<CommentDto> getCommentById(
            @Parameter(description = "Id of post to be searched") @PathVariable(name = "postId") long postId,
            @Parameter(description = "Id of comment to be searched") @PathVariable(name = "commentId") long commentId
    ) {
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
    @Operation(summary = "Update a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successfully updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public ResponseEntity<CommentDto> updateComment(
            @Parameter(description = "Id of the post to which the comment belongs") @PathVariable(name = "postId") long postId,
            @Parameter(description = "Id of the comment you want to modify") @PathVariable(name = "commentId") long commentId,
            @Valid @RequestBody CommentDto commentDto
    ) {
        return ResponseEntity.ok(commentService.updateComment(postId, commentId, commentDto));
    }

    /**
     * Delete a comment from a post
     * @param postId Post id
     * @param commentId Comment id
     * @return A String
     */
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @Operation(summary = "Delete a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successfully deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public ResponseEntity<String>  deleteComment(
            @Parameter(description = "Id of the post to which the comment belongs") @PathVariable(name = "postId") long postId,
            @Parameter(description = "Id of the comment you want to delete") @PathVariable(name = "commentId") long commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
