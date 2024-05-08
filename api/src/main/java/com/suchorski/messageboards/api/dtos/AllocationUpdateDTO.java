package com.suchorski.messageboards.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllocationUpdateDTO {

    public boolean moderator;

    public boolean administrator;

}
