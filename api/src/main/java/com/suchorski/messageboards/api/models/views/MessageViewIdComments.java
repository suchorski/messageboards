package com.suchorski.messageboards.api.models.views;

import java.time.Instant;
import java.util.List;

public interface MessageViewIdComments {

    Long getId();

    String getText();

    Instant getCreationDate();

    Instant getLastUpdateDate();

    Instant getFinalizationDate();

    List<CommentViewIdTextUser> getComments();

    UserViewNameNicknameOmRankNoId getAuthor();

}
