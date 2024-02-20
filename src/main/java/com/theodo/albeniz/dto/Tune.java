package com.theodo.albeniz.dto;

import com.theodo.albeniz.validation.NotAChildrenSong;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NotAChildrenSong
public class Tune {
    private UUID id;

    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotNull(message = "Title is also mandatory")
    private String author;
}
