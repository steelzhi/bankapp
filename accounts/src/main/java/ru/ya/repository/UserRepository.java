package ru.ya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ya.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);

    @Query("""
            SELECT id
            FROM User
            WHERE login = :login
            """)
    List<Integer> getIdByLogin(String login);

    @Query(value = """
            UPDATE accounts.users
            SET password = :password
            WHERE login = :login
            """, nativeQuery = true)
    User changePassword(String password, String login);
}
