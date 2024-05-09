package com.suchorski.messageboards.api.models.views;

public interface AllocationsViewUserTypes {

    Long getId();

    UserViewNameNicknameCompanyRankNoId getUser();

    boolean getAdministrator();

    boolean getModerator();

}
