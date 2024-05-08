package com.suchorski.messageboards.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.suchorski.messageboards.api.models.Message;
import com.suchorski.messageboards.api.models.User;
import com.suchorski.messageboards.api.models.views.MessageViewAuthor;
import com.suchorski.messageboards.api.models.views.MessageViewIdComments;

@RepositoryRestResource(path = "messages")
public interface MessageRepository extends CrudRepository<Message, Long> {

    public static final String MESSAGE_NOT_FOUND = "Mensagem não encontrada.";
    public static final String MESSAGE_NOT_THAT_USER = "Mensagem não pertence a esse usuário.";
    public static final String USER_NOT_ALLOCATED = "Usuário não alocado nesse quadro de avisos.";

    public Optional<Message> findByIdAndAuthor(Long id, User author);

    public Optional<MessageViewIdComments> findByIdAndBoardAllocationsUser(Long id, User user);

    public List<MessageViewAuthor> findAllByBoardIdAndFinalizationDateIsNullAndBoardAllocationsUser(Long id, User user);

}
