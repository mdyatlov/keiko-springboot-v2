package com.theodo.albeniz.controller;

import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.services.selection.UserSelectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/selection")
public class UserSelectionController {

    private final UserSelectionService userSelectionService;

    @PostMapping("/{tuneId}")
    public ResponseEntity<UUID> add(@PathVariable("tuneId") UUID musicId, Principal principal) {
        String userName = principal.getName();
        return ResponseEntity.ok(userSelectionService.addToSelection(musicId, userName));
    }

    @DeleteMapping("/{tuneId}")
    public ResponseEntity<UUID> remove(@PathVariable("tuneId") UUID musicId, Principal principal) {
        String userName = principal.getName();
        return ResponseEntity.ok(userSelectionService.removeFromSelection(musicId, userName));
    }
    @GetMapping()
    public ResponseEntity<List<Tune>> get(Principal principal) {
        String userName = principal.getName();
        return ResponseEntity.ok(userSelectionService.getSelection(userName));
    }
}
