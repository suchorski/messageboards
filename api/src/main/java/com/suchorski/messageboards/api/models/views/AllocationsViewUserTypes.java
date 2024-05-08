package com.suchorski.messageboards.api.models.views;

public interface AllocationsViewUserTypes {

    Long getId();

    UserViewNameNicknameOmRankNoId getUser();

    boolean getAdministrator();

    boolean getModerator();

}
