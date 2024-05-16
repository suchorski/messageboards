package com.suchorski.messageboards.api.models.views;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface MessageViewIdComments {

    Long getId();

    String getText();

    Instant getCreationDate();

    Instant getLastUpdateDate();

    Instant getFinalizationDate();

    LocalDateTime getDeadline();

    List<CommentViewIdTextUser> getComments();

    UserViewNameNicknameCompanyRankNoId getAuthor();

}
