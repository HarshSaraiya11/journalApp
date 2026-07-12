package com.myjournalApp.journalApp.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

//import org.springframework.data.annotation.Id; 

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;

@Entity
public class JournalEntry {
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "journal_entry_seq")
    @SequenceGenerator(name = "journal_entry_seq", sequenceName = "journal_entry_sequence", allocationSize = 1)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Column(name="TITLE", nullable = false)
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Column(name="CONTENT")
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    @Column(name="create_dt")
    private LocalDateTime createDate;
    public LocalDateTime getcreateDate() {
        return createDate;
    }
    public void setcreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Column(name="last_update_dt")
    private LocalDateTime updtDate;
    public LocalDateTime getupdtDate() {
        return updtDate;
    }
    public void setupdtDate(LocalDateTime updtDate) {
        this.updtDate = updtDate;
    }
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User users;
    public void setUsers(User users) {
        this.users = users;
    }
    public User getUsers() {
        return users;
    }
}
