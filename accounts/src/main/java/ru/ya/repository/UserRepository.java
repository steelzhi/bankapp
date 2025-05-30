package ru.ya.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ya.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);

    @Query("""
            SELECT u
            FROM User u
            WHERE login != :login
            """)
    List<User> findAllOtherUsersExceptUser(String login);

    @Query("""
            SELECT id
            FROM User
            WHERE login = :login
            """)
    List<Integer> getIdByLogin(String login);

/*    @Query(value = """
            UPDATE accounts.users
            SET password = :password
            WHERE login = :login
            """, nativeQuery = true)
    User changePassword(String password, String login);*/

/*    @Query("""
            DELETE FROM User
            WHERE login = :login
            """)
    void deleteUserByLogin(String login);*/

    // Без 2 аннотаций ниже запрос выдаст ошибку
    @Modifying
    @Transactional
    @Query("""
            UPDATE User
            SET password = :password
            WHERE id = :id
            """)
    void changePassword(String password, int id);

    @Modifying
    @Transactional
    @Query("""
            UPDATE User
            SET name = :name,
                surname = :surname,
                birthdate = :birthdate
            WHERE id = :id
            """)
    void changeOtherData(String name, String surname, LocalDate birthdate, int id);
}
