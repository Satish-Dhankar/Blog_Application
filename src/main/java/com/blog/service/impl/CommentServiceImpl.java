package com.blog.service.impl;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.exception.BlogAPIException;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.CommentDto;
import com.blog.repositories.CommentRepository;
import com.blog.repositories.PostRepository;
import com.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;

    private ModelMapper mapper;

    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository,ModelMapper mapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper=mapper;
    }

    @Override
    public CommentDto saveComment(Long postId, CommentDto commentDto) {
        Post post=postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post not found with id :"+postId)
        );
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post=postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with id: "+postId)
        );
        Comment comment=commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Comment not found with id: "+commentId)
        );

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException("Comment doesn't belongs to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        Post post=postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with id: "+postId)
        );
        Comment comment=commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Comment not found with id: "+commentId)
        );

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException("Comment does not belong to post");
        }
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        comment.setName(commentDto.getName());
        Comment updateComment = commentRepository.save(comment);
        return mapToDto(updateComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post=postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with id :"+postId)
        );
        Comment comment=commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Comment not found with id :"+commentId)
        );
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException("Comment does not belong to post");
        }
        commentRepository.deleteById(commentId);
    }

    Comment mapToEntity(CommentDto commentDto){
//        Comment comment=new Comment();
//        comment.setBody(commentDto.getBody());
//        comment.setEmail(commentDto.getEmail());
//        comment.setName(commentDto.getName());
        Comment comment = mapper.map(commentDto, Comment.class);
        return comment;
    }
    CommentDto mapToDto(Comment comment){
//        CommentDto dto=new CommentDto();
//        dto.setId(comment.getId());
//        dto.setBody(comment.getBody());
//        dto.setEmail(comment.getEmail());
//        dto.setName(comment.getName());
        CommentDto dto = mapper.map(comment, CommentDto.class);
        return dto;
    }
}
