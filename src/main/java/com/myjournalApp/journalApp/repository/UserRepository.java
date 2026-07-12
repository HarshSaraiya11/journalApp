package com.myjournalApp.journalApp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myjournalApp.journalApp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByUserName(String userName);
}
