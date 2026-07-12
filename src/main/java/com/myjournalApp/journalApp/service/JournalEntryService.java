package com.myjournalApp.journalApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myjournalApp.journalApp.dto.JournalRequestDto;
import com.myjournalApp.journalApp.dto.JournalResponseDto;
import com.myjournalApp.journalApp.entity.JournalEntry;
import com.myjournalApp.journalApp.entity.User;
import com.myjournalApp.journalApp.repository.JournalEntryRepository;

@Service
public class JournalEntryService {
    
    
    private JournalEntryRepository journalEntryRepository;
    private UserService userService;    

    public JournalEntryService(JournalEntryRepository journalEntryRepository, UserService userService) {
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    @Transactional
    public JournalResponseDto createJournalEntry(JournalRequestDto requestDto, String userName) {
        
        User user = userService.findByUserName(userName);
        LocalDateTime now = LocalDateTime.now();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setTitle(requestDto.getTitle());
        journalEntry.setContent(requestDto.getContent());
        journalEntry.setcreateDate(now);
        journalEntry.setUsers(user);
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);

        JournalResponseDto response = new JournalResponseDto(savedEntry.getTitle(), savedEntry.getContent());
        return response;
    }

    @Transactional
    public void updateJournalEntry(JournalEntry journalEntry, Long id, String userName) {
        LocalDateTime now = LocalDateTime.now();
        JournalEntry existingEntry = getJournalEntryById(id).orElseThrow(() -> new RuntimeException("Journal entry not found"));
        if (!existingEntry.getUsers().getUserName().equals(userName)) {
           throw new RuntimeException("Unauthorized");
        } 
        existingEntry.setTitle(journalEntry.getTitle());
        existingEntry.setContent(journalEntry.getContent());
        existingEntry.setupdtDate(now);
        journalEntryRepository.save(existingEntry);
    }

    public List<JournalEntry> getEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getJournalEntryById(Long id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public void deleteJournalEntryById(Long id, String username) {
        User user = userService.findByUserName(username);
        user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
        JournalEntry entry = journalEntryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Journal not found"));
        if (entry.getUsers().getId().equals(user.getId())) {
            journalEntryRepository.deleteById(id);    
        }else{
            throw new RuntimeException("Unauthorized");
        }
    }

}
