package com.theodo.albeniz.controller;

import com.theodo.albeniz.dto.Tune;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("library")
public class LibraryController {
    @GetMapping("music")
    public Collection<Tune> getMusic(){
        return List.of(new Tune("Thriller", "MJ"), new Tune("Prelude and Fugue in C minor", "Bach"));
    }
}
