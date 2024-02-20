package com.theodo.albeniz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class Tune {
    private UUID id;
    private String title;
    private String author;
}
