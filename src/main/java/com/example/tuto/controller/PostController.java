package com.example.tuto.controller;

import com.example.tuto.dto.PostDto;
import com.example.tuto.dto.PostResponse;
import com.example.tuto.service.PostService;
import com.example.tuto.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Create a post
     * @param postDto New post
     * @return Added post
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    /**
     * Get all posts
     * @param pageNo Page number
     * @param pageSize Page size
     * @param sortBy Sort by property
     * @param sortDir Sorting direction ("asc" or "desc")
     * @return List of posts
     */
    @GetMapping
    @Operation(summary = "Get all posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts successfully recovered",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    /**
     * Get a post by id
     * @param id Post id
     * @return Found post
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a post by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content)
    })
    public ResponseEntity<PostDto> getPostById(@Parameter(description = "Id of post to be searched") @PathVariable(name = "id") long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    /**
     * Update a post
     * @param postDto Post to update
     * @param id Post id
     * @return Updated post
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post successfully updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id){
       PostDto postResponse = postService.updatePost(postDto, id);
       return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    /**
     * Delete a post
     * @param id Post id
     * @return A String
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post successfully deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }
}
