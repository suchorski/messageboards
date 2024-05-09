package com.suchorski.messageboards.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.suchorski.messageboards.api.models.User;

@RepositoryRestResource(path = "users")
public interface UserRepository extends CrudRepository<User, Long> {

    public static final String ALLOCATED_USER_NOT_FOUND = "Usuário alocado não encontrado.";

    public Optional<User> findByCpf(String cpf);

    public Optional<User> findTop1ByOrderByLastUpdateAsc();

}
