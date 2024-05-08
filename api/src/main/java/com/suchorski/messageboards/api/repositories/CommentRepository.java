package com.suchorski.messageboards.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.suchorski.messageboards.api.models.Comment;
import com.suchorski.messageboards.api.models.User;

@RepositoryRestResource(path = "comments")
public interface CommentRepository extends CrudRepository<Comment, Long> {

    public static final String MESSAGE_NOT_THAT_USER = "Comentário não pertence a esse usuário.";

    public Optional<Comment> findByIdAndAuthor(Long id, User user);

    public Optional<Comment> findTop1ByMessageIdOrderByCreationDateDesc(Long messageId);

}
