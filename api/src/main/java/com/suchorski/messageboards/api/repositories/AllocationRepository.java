package com.suchorski.messageboards.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.suchorski.messageboards.api.models.Allocation;
import com.suchorski.messageboards.api.models.Board;
import com.suchorski.messageboards.api.models.User;
import com.suchorski.messageboards.api.models.views.AllocationsViewUserTypes;

@RepositoryRestResource(path = "allocations")
public interface AllocationRepository extends CrudRepository<Allocation, Long> {

    public static final String ALLOCATION_NOT_FOUND = "Alocação não encontrada.";
    public static final String SAME_USER = "Você não pode alocar você mesmo.";
    public static final String USER_NOT_ALLOCATED = "Usuário não alocado nesse quadro de avisos.";
    public static final String USER_NOT_ALLOCATED_AS_ADMINISTRATOR = "Usuário não alocado como administrador nesse quadro de avisos.";
    public static final String USER_NOT_ALLOCATED_AS_ADMINISTRATOR_OR_MODERATOR = "Usuário não alocado como administrador ou moderador nesse quadro de avisos.";

    public Optional<Allocation> findByUserAndBoard(User user, Board board);

    public Optional<Allocation> findByUserAndBoardId(User user, Long id);

    public List<AllocationsViewUserTypes> findAllByBoardId(Long id);

}
