package com.theodo.albeniz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("library")
public class LibraryController {
    @GetMapping("music")
    public Collection<String> getMusic(){
        return List.of("Thriller", "Prelude and Fugue in C minor");
    }
}
