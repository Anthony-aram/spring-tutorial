package com.example.tuto.service.impl;

import com.example.tuto.dto.PostDto;
import com.example.tuto.dto.PostResponse;
import com.example.tuto.entity.Post;
import com.example.tuto.exception.ResourceNotFoundException;
import com.example.tuto.repository.PostRepository;
import com.example.tuto.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create a post
     * @param postDto Post dto
     * @return Created post
     */
    @Override
    public PostDto createPost(PostDto postDto) {

        // convert DTO to entity
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);

        // convert entity to DTO
        return mapToDTO(newPost);
    }

    /**
     * Get all posts
     * @return All posts
     */
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        // Get content from page object
        List<Post> postsList = posts.getContent();

        List<PostDto> content = postsList.stream().map(this::mapToDTO).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    /**
     * Get a post by id
     * @param id Post id
     * @return Found post
     */
    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    /**
     * Update a post
     * @param postDto Post
     * @param id Post id
     * @return Updated post
     */
    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        // get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    /**
     * Delete a post
     * @param id Post id
     */
    @Override
    public void deletePostById(long id) {
        // get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    /**
     * Convert a post entity to post dto
     * @param post Post entity
     * @return Post dto
     */
    private PostDto mapToDTO(Post post){
        return modelMapper.map(post, PostDto.class);
    }

    /**
     * Convert a post dto to post entity
     * @param postDto Post dto
     * @return Post entity
     */
    private Post mapToEntity(PostDto postDto){
        return modelMapper.map(postDto, Post.class);
    }
}
