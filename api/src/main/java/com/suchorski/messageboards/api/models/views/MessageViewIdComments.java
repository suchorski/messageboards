package com.suchorski.messageboards.api.models.views;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface MessageViewIdComments {

    Long getId();

    String getText();

    Instant getCreationDate();

    Instant getLastUpdateDate();

    Instant getFinalizationDate();

    LocalDate getDeadline();

    List<CommentViewIdTextUser> getComments();

    UserViewNameNicknameCompanyRankNoId getAuthor();

}
