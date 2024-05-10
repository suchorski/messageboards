package com.suchorski.messageboards.api.controllers;

import java.time.Instant;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.suchorski.messageboards.api.components.AuthenticationUtils;
import com.suchorski.messageboards.api.dtos.DeadlineDTO;
import com.suchorski.messageboards.api.dtos.MessageDTO;
import com.suchorski.messageboards.api.models.Message;
import com.suchorski.messageboards.api.repositories.AllocationRepository;
import com.suchorski.messageboards.api.repositories.BoardRepository;
import com.suchorski.messageboards.api.repositories.MessageRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RepositoryRestController(path = "messages")
@AllArgsConstructor
@Log4j2
public class MessageController {

    private AuthenticationUtils authenticationUtils;
    private MessageRepository messageRepository;
    private BoardRepository boardRepository;

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> save(@NonNull @Valid @RequestBody MessageDTO newMessage,
            Authentication authentication) {
        log.debug("Saving message...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var board = boardRepository
                .findByIdAndAllocationsUser(newMessage.getBoard().getId(), user)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                BoardRepository.USER_NOT_ALLOCATED));
        final var message = new Message(newMessage.getText(), board, user);
        message.setDeadline(newMessage.getDeadline());
        return ResponseEntity.ok(messageRepository.save(message));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWithComments(@PathVariable Long id, Authentication authentication) {
        log.debug("Getting message...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var message = messageRepository
                .findByIdAndBoardAllocationsUser(id, user)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                MessageRepository.USER_NOT_ALLOCATED));
        return ResponseEntity.ok(message);
    }

    @GetMapping("/byBoardId/{id}")
    public ResponseEntity<?> listByBoard(@PathVariable Long id, Authentication authentication) {
        log.debug("Listing messages by board...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        boardRepository
                .findByIdAndAllocationsUser(id, user)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                BoardRepository.USER_NOT_ALLOCATED));
        return ResponseEntity
                .ok(messageRepository.findAllByBoardIdAndFinalizationDateIsNullAndBoardAllocationsUser(
                        id, user));
    }

    @PutMapping("/{id}/deadline")
    @ResponseBody
    public ResponseEntity<?> updateDeadline(@PathVariable Long id, @Valid @RequestBody DeadlineDTO deadlineDTO,
            Authentication authentication) {
        log.debug("Updateing deadline message...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var message = messageRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                MessageRepository.MESSAGE_NOT_FOUND));
        if (!message.getAuthor().equals(user)) {
            message.getBoard().getAllocations().stream()
                    .filter(a -> a.getUser().equals(user) && a.getAdministrator())
                    .findAny()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                            AllocationRepository.USER_NOT_ALLOCATED_AS_ADMINISTRATOR));
        }
        message.setDeadline(deadlineDTO.getDeadline());
        messageRepository.save(message);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> finalize(@PathVariable Long id, Authentication authentication) {
        log.debug("Finalizing message...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var message = messageRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                MessageRepository.MESSAGE_NOT_FOUND));
        if (!message.getAuthor().equals(user)) {
            message.getBoard().getAllocations().stream()
                    .filter(a -> a.getUser().equals(user) && a.getAdministrator())
                    .findAny()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                            AllocationRepository.USER_NOT_ALLOCATED_AS_ADMINISTRATOR));
        }
        message.setFinalizationDate(Instant.now());
        messageRepository.save(message);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        log.debug("Deleting message...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var message = messageRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageRepository.MESSAGE_NOT_FOUND));
        if (!message.getAuthor().equals(user)) {
            message.getBoard().getAllocations().stream()
                    .filter(a -> a.getUser().equals(user) && a.getAdministrator())
                    .findAny()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                            AllocationRepository.USER_NOT_ALLOCATED_AS_ADMINISTRATOR));
        }
        messageRepository.delete(message);
        return ResponseEntity.noContent().build();
    }

}
