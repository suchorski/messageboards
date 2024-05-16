package com.suchorski.messageboards.api.models.views;

import java.time.Instant;
import java.time.LocalDateTime;

public interface MessageViewAuthor {

    Long getId();

    String getText();

    Instant getCreationDate();

    Instant getLastUpdateDate();

    Instant getFinalizationDate();

    LocalDateTime getDeadline();

    UserViewNameNicknameCompanyRankNoId getAuthor();

}
