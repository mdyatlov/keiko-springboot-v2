package com.theodo.albeniz.controller;

import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.services.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping("music")
    public Collection<Tune> getMusic(@RequestParam(name = "query", required = false) String query){
        return libraryService.getAll(query);
    }

    @GetMapping("music/{id}")
    public Tune getMusic(@PathVariable UUID id){
        return libraryService.getOne(id);
    }

    @PostMapping("music")
    public Tune addTune(@Valid  @RequestBody Tune tune){
        libraryService.addTune(tune);
        return libraryService.getOne(tune.getId());
    }

}
