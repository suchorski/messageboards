package com.suchorski.messageboards.api.controllers;

import javax.naming.NamingException;

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
import com.suchorski.messageboards.api.components.LdapUtils;
import com.suchorski.messageboards.api.dtos.AllocationDTO;
import com.suchorski.messageboards.api.dtos.AllocationUpdateDTO;
import com.suchorski.messageboards.api.models.Allocation;
import com.suchorski.messageboards.api.models.User;
import com.suchorski.messageboards.api.repositories.AllocationRepository;
import com.suchorski.messageboards.api.repositories.BoardRepository;
import com.suchorski.messageboards.api.repositories.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RepositoryRestController(path = "allocations")
@AllArgsConstructor
@Log4j2
public class AllocationsController {

    private AuthenticationUtils authenticationUtils;
    private AllocationRepository allocationRepository;
    private UserRepository userRepository;
    private BoardRepository boardRepository;
    private LdapUtils ldapUtils;

    @Transactional
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> save(@NonNull @Valid @RequestBody AllocationDTO allocationDTO,
            Authentication authentication) throws ResponseStatusException, NamingException {
        log.debug("Saving allocation...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var allocatedUserDatabase = userRepository.findByCpf(allocationDTO.getCpf());
        User allocatedUser;
        if (allocatedUserDatabase.isEmpty()) {
            final var allocatedUserLdap = ldapUtils
                    .findByCpf(allocationDTO.getCpf())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            UserRepository.ALLOCATED_USER_NOT_FOUND));
            allocatedUser = userRepository.save(allocatedUserLdap);
        } else {
            allocatedUser = allocatedUserDatabase.get();
        }
        if (user.equals(allocatedUser)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, AllocationRepository.SAME_USER);
        }
        final var allocatedBoard = boardRepository
                .findByIdAndAllocationsUser(allocationDTO.getBoard().getId(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        BoardRepository.USER_NOT_ALLOCATED));
        final var allocation = allocationRepository
                .findByUserAndBoard(user, allocatedBoard)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        AllocationRepository.USER_NOT_ALLOCATED));
        if (!allocation.getAdministrator() && !allocation.getModerator()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    AllocationRepository.USER_NOT_ALLOCATED_AS_ADMINISTRATOR_OR_MODERATOR);
        }
        final var newaAllocation = new Allocation(allocatedUser, allocatedBoard,
                allocationDTO.isAdministrator(), allocationDTO.isModerator());
        return ResponseEntity.ok(allocationRepository.save(newaAllocation));
    }

    @GetMapping("/byBoardId/{id}")
    public ResponseEntity<?> listByBoard(@PathVariable Long id, Authentication authentication) {
        log.debug("Listing allocations by board...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var allocation = allocationRepository
                .findByUserAndBoardId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        AllocationRepository.USER_NOT_ALLOCATED));
        if (!allocation.getAdministrator() && !allocation.getModerator()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    AllocationRepository.USER_NOT_ALLOCATED_AS_ADMINISTRATOR_OR_MODERATOR);
        }
        return ResponseEntity.ok(allocationRepository.findAllByBoardId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @NonNull @Valid @RequestBody AllocationUpdateDTO allocationUpdateDTO,
            Authentication authentication) {
        log.debug("Updating allocation...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var allocationToUpdate = allocationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        AllocationRepository.ALLOCATION_NOT_FOUND));
        final var board = allocationToUpdate.getBoard();
        final var allocation = allocationRepository
                .findByUserAndBoard(user, board)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        AllocationRepository.USER_NOT_ALLOCATED));
        if (!allocation.getAdministrator()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    AllocationRepository.USER_NOT_ALLOCATED_AS_ADMINISTRATOR);
        }
        if (allocationToUpdate.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, AllocationRepository.SAME_USER);
        }
        allocationToUpdate.setAdministrator(allocationUpdateDTO.isAdministrator());
        allocationToUpdate.setModerator(allocationUpdateDTO.isModerator());
        return ResponseEntity.ok(allocationRepository.save(allocationToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        log.debug("Deleting allocation...");
        final var user = authenticationUtils.findOrCreateUser(authentication);
        final var allocationToDelete = allocationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        AllocationRepository.ALLOCATION_NOT_FOUND));
        final var board = allocationToDelete.getBoard();
        final var allocation = allocationRepository
                .findByUserAndBoard(user, board)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        AllocationRepository.USER_NOT_ALLOCATED));
        if (!allocation.getAdministrator()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    AllocationRepository.USER_NOT_ALLOCATED_AS_ADMINISTRATOR);
        }
        allocationRepository.delete(allocationToDelete);
        return ResponseEntity.noContent().build();
    }

}
