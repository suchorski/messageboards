package com.suchorski.messageboards.api.dtos;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
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
public class MessageDTO {

    @NotBlank
    public String text;

    @Nullable
    public LocalDateTime deadline;

    @NotNull
    public IdDTO board;

}
