package com.suchorski.messageboards.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllocationDTO {

    @NotBlank
    public String cpf;

    @NotNull
    public IdDTO board;

    public boolean moderator;

    public boolean administrator;

}
