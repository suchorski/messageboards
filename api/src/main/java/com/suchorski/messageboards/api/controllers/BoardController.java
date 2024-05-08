package com.suchorski.messageboards.api.controllers;

import org.springframework.data.domain.Pageable;
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
import com.suchorski.messageboards.api.models.Allocation;
import com.suchorski.messageboards.api.models.Board;
import com.suchorski.messageboards.api.repositories.AllocationRepository;
import com.suchorski.messageboards.api.repositories.BoardRepository;
import com.suchorski.messageboards.api.roles.HasAdminRole;
import com.suchorski.messageboards.api.utils.ResponseGenerator;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RepositoryRestController(path = "boards")
@AllArgsConstructor
@Log4j2
public class BoardController {

    private AuthenticationUtils authenticationUtils;
    private ResponseGenerator responseGenerator;
    private BoardRepository boardRepository;
    private AllocationRepository allocationRepository;

    @Transactional
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> save(@NonNull @Valid @RequestBody Board board, Authentication authentication) {
        log.debug("Saving board...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        boardRepository.save(board);
        allocationRepository.save(new Allocation(user, board, true, true));
        return ResponseEntity.ok(board);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(Authentication authentication) {
        log.debug("Listing boards...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        return ResponseEntity.ok(responseGenerator.content(boardRepository.findAllByAllocationsUser(user)));
    }

    @GetMapping("/all")
    @HasAdminRole
    public ResponseEntity<?> all(Pageable pageRequest, Authentication authentication) {
        log.debug("Listing all boards...");
        authenticationUtils.findOrCreateUser(authentication);
        return ResponseEntity.ok(boardRepository.findAll(pageRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @NonNull @Valid @RequestBody Board updatedBoard,
            Authentication authentication) {
        log.debug("Updating board...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var board = boardRepository
                .findByIdAndAllocationsUser(id, user)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, BoardRepository.USER_NOT_ALLOCATED));
        final var allocation = allocationRepository
                .findByUserAndBoard(user, board)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        AllocationRepository.USER_NOT_ALLOCATED));
        if (!allocation.getAdministrator()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    AllocationRepository.USER_NOT_ALLOCATED_AS_ADMINISTRATOR);
        }
        board.setName(updatedBoard.getName());
        return ResponseEntity.ok(boardRepository.save(board));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        log.debug("Deleting board...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var board = boardRepository
                .findByIdAndAllocationsUser(id, user)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, BoardRepository.USER_NOT_ALLOCATED));
        final var allocation = allocationRepository
                .findByUserAndBoard(user, board)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        AllocationRepository.USER_NOT_ALLOCATED));
        if (!allocation.getAdministrator()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    AllocationRepository.USER_NOT_ALLOCATED_AS_ADMINISTRATOR);
        }
        boardRepository.delete(board);
        return ResponseEntity.noContent().build();
    }

}
