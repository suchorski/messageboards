package com.suchorski.messageboards.api.dtos;

import java.time.LocalDate;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeadlineDTO {

    @Nullable
    public LocalDate deadline;

}
