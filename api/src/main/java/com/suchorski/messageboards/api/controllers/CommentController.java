package com.suchorski.messageboards.api.controllers;

import java.time.Instant;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.suchorski.messageboards.api.components.AuthenticationUtils;
import com.suchorski.messageboards.api.dtos.CommentDTO;
import com.suchorski.messageboards.api.models.Comment;
import com.suchorski.messageboards.api.repositories.CommentRepository;
import com.suchorski.messageboards.api.repositories.MessageRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RepositoryRestController(path = "comments")
@AllArgsConstructor
@Log4j2
public class CommentController {

    private AuthenticationUtils authenticationUtils;
    private CommentRepository commentRepository;
    private MessageRepository messageRepository;

    @Transactional
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> save(@NonNull @Valid @RequestBody CommentDTO newComment, Authentication authentication) {
        log.debug("Saving comment...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var message = messageRepository
                .findById(newComment.getMessage().getId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.MESSAGE_NOT_FOUND));
        final var comment = new Comment(newComment.getText(), message, user);
        message.setLastUpdateDate(Instant.now());
        messageRepository.save(message);
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        log.debug("Deleting comment...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var comment = commentRepository
                .findByIdAndAuthor(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        CommentRepository.MESSAGE_NOT_THAT_USER));
        final var lastComment = commentRepository
                .findTop1ByMessageIdOrderByCreationDateDesc(comment.getMessage().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        MessageRepository.MESSAGE_NOT_FOUND));
        if (lastComment.equals(comment)) {
            commentRepository.delete(comment);
            return ResponseEntity.ok(true);
        } else {
            comment.setDeleted(true);
            commentRepository.save(comment);
            return ResponseEntity.ok(false);
        }
    }

}
