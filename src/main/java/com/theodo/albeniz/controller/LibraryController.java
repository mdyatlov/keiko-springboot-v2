package com.theodo.albeniz.controller;

import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.exceptions.NotFoundException;
import com.theodo.albeniz.services.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Tune> getMusic(@PathVariable UUID id){
        if(!libraryService.isExist(id)){
            //A first way to return an HTTP StatusCode
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libraryService.getOne(id));
    }

    @DeleteMapping("music/{id}")
    public void deleteTune(@PathVariable UUID id) throws NotFoundException {
        boolean removed = libraryService.removeTune(id);
        if(!removed){
            // Another way to return an HTTP StatusCode: throw an home-made exception that will be catch in a GlobalExceptionHandler
            throw new NotFoundException();
        }
    }

    @PostMapping("music")
    @ResponseStatus(HttpStatus.CREATED) // A third way to return an HTTP StatusCode
    public Tune addTune(@Valid  @RequestBody Tune tune) {
        libraryService.addTune(tune);
        return libraryService.getOne(tune.getId());
    }

    @PutMapping("music")
    @ResponseStatus(HttpStatus.OK)
    public Tune updateTune(@Valid  @RequestBody Tune tune) {
        libraryService.modifyTune(tune);
        return libraryService.getOne(tune.getId());
    }
}
