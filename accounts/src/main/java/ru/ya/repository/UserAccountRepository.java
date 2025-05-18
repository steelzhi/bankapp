package ru.ya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ya.model.User;

@Repository
public interface UserAccountRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
