package com.suchorski.messageboards.api.models.views;

import java.time.Instant;

public interface CommentViewIdTextUser {

    Long getId();

    String getText();

    Instant getCreationDate();

    Boolean getDeleted();

    UserViewNameNicknameCompanyRankNoId getAuthor();

}
