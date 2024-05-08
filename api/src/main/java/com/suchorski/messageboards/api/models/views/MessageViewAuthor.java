package com.suchorski.messageboards.api.models.views;

import java.time.Instant;

public interface MessageViewAuthor {

    Long getId();

    String getText();

    Instant getCreationDate();

    Instant getLastUpdateDate();

    Instant getFinalizationDate();

    UserViewNameNicknameOmRankNoId getAuthor();

}
