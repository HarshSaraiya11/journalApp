package com.myjournalApp.journalApp.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.myjournalApp.journalApp.dto.JournalRequestDto;
import com.myjournalApp.journalApp.dto.JournalResponseDto;
import com.myjournalApp.journalApp.entity.JournalEntry;
import com.myjournalApp.journalApp.entity.User;
import com.myjournalApp.journalApp.service.JournalEntryService;
import com.myjournalApp.journalApp.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 { 

    
    private JournalEntryService journalEntryService;
    private UserService userService;

    public JournalEntryControllerV2(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<String> all =  user.getJournalEntries()
        .stream().map(entry -> entry.getTitle()).toList();
        if (all!= null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchJournalEntrybyTitle(@RequestParam String title, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all =  user.getJournalEntries();
        List<JournalEntry> filtered = all.stream()
                .filter(entry -> entry.getTitle() != null && entry.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
            
        if (filtered!= null && !filtered.isEmpty()) {
            return new ResponseEntity<>(filtered, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<JournalResponseDto> createEntry(@Valid @RequestBody JournalRequestDto journalRequestDto, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String userName = authentication.getName();
            JournalResponseDto respDto= journalEntryService.createJournalEntry(journalRequestDto, userName);
            return ResponseEntity.status(HttpStatus.CREATED).body(respDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable Long myId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userName = authentication.getName();
        Optional<JournalEntry> entry = journalEntryService.getJournalEntryById(myId);
         if (entry.isPresent()) {
             return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delete/id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable Long myId, Authentication authentication) { 
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName(); 
        journalEntryService.deleteJournalEntryById(myId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("update/id/{Id}")
    public ResponseEntity<?> updateEntry(
        @PathVariable Long Id,
        @RequestBody JournalEntry myEntry,
        Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName(); 
        journalEntryService.updateJournalEntry(myEntry, Id, username);
        return ResponseEntity.ok().build(); 
    }
}

