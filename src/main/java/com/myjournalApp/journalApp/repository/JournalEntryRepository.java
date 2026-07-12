package com.myjournalApp.journalApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myjournalApp.journalApp.entity.JournalEntry;
import com.myjournalApp.journalApp.entity.User;
@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    Optional<JournalEntry> findByIdAndUsers(Long id, User user);
}
