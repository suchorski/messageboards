package com.suchorski.messageboards.api.dtos;

import java.time.LocalDateTime;

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
    public LocalDateTime deadline;

}
