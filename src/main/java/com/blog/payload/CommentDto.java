package com.blog.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentDto {

    private long id;
    @NotEmpty
    @Size(min = 3, message = "Name should have at least 3 characters")
    private String name;
    @NotEmpty
    @Email(message = "Email id is compulsory")
    private String email;
    @NotEmpty
    @Size(min = 4, message = "Comment body should have at least 4 characters")
    private String body;

}