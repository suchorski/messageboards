package com.suchorski.messageboards.api.models.views;

import java.time.Instant;
import java.time.LocalDate;

public interface MessageViewAuthor {

    Long getId();

    String getText();

    Instant getCreationDate();

    Instant getLastUpdateDate();

    Instant getFinalizationDate();

    LocalDate getDeadline();

    UserViewNameNicknameCompanyRankNoId getAuthor();

}
