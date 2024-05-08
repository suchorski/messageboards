package com.suchorski.messageboards.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.lang.NonNull;

import com.suchorski.messageboards.api.models.Board;
import com.suchorski.messageboards.api.models.User;

@RepositoryRestResource(path = "boards")
public interface BoardRepository extends CrudRepository<Board, Long>, PagingAndSortingRepository<Board, Long> {

    public static final String USER_NOT_ALLOCATED = "Usuário não alocado nesse quadro de avisos.";

    public Optional<Board> findByIdAndAllocationsUser(Long id, User user);

    public List<Board> findAllByAllocationsUser(User user);

    public @NonNull Page<Board> findAll(@NonNull Pageable pageable);

}
