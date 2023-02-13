package com.example.tuto.service.impl;

import com.example.tuto.dto.CommentDto;
import com.example.tuto.dto.PostDto;
import com.example.tuto.entity.Comment;
import com.example.tuto.entity.Post;
import com.example.tuto.exception.BlogAPIException;
import com.example.tuto.exception.ResourceNotFoundException;
import com.example.tuto.repository.CommentRepository;
import com.example.tuto.repository.PostRepository;
import com.example.tuto.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    /**
     * Create a comment associated to a post
     * @param postId Associated post
     * @param commentDto Comment to add to the post
     * @return Created comment
     */
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        // Retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // Set post to comment entity
        comment.setPost(post);

        // Save comment entity to db
        Comment createdComment = commentRepository.save(comment);

        return mapToDTO(createdComment);
    }

    /**
     * Get all comments from a post
     * @param postId Post id
     * @return Post comments
     */
    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map((comment) -> mapToDTO(comment)).collect(Collectors.toList());
    }

    /**
     * Get a comment from a post
     * @param postId Post id
     * @param commentId Comment id
     * @return A comment
     */
    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        // Retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // Retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", postId));

        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        return mapToDTO(comment);
    }

    /**
     * Update a comment from a post
     * @param postId Post id
     * @param commentId Comment id
     * @param commentDto Comment to update
     * @return Updated comment
     */
    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        // Retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // Retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", postId));

        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }

    /**
     * Delete a comment from a post
     * @param postId Post id
     * @param commentId Comment id
     * @return A String
     */
    @Override
    public void deleteComment(long postId, long commentId) {
        // Retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // Retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", postId));

        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }

        commentRepository.delete(comment);
    }


    /**
     * Convert a comment entity to comment dto
     * @param comment Comment entity
     * @return Comment dto
     */
    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    /**
     * Convert a comment dto to comment entity
     * @param commentDto Comment dto
     * @return Comment entity
     */
    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}
