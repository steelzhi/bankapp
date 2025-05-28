package ru.ya.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ya.model.BankAccount;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    List<BankAccount> findAllById(int id);

    @Query("""
            SELECT accountValue
            FROM BankAccount
            WHERE id = :id
            """)
    double findAccountValueById(int id);

    @Modifying // Без этой аннотации не работают методы UPDATE, DELETE
    @Transactional
    @Query("""
            UPDATE BankAccount
            SET accountValue = accountValue + :summand
            WHERE accountNumber = :accountNumber
            """)
    void increaseSumOnBankAccount(double summand, String accountNumber);

    @Modifying // Без этой аннотации не работают методы UPDATE, DELETE
    @Transactional
    @Query("""
            UPDATE BankAccount
            SET accountValue = accountValue - :deductible
            WHERE accountNumber = :accountNumber
            """)
    void decreaseSumOnBankAccount(double deductible, String accountNumber);

    BankAccount findByAccountNumber(String accountNumber);

    @Query("""
            SELECT ba.accountValue
            FROM BankAccount ba
            LEFT JOIN User u ON u.id = ba.user.id
            WHERE u.id = :id
                AND ba.accountNumber = :accountNumber
            """)
    double getAccountValueByIdAndAccountNumber(int id, String accountNumber);

    @Query("""
            SELECT currency
            FROM BankAccount
            WHERE accountNumber = :accountNumber
            """)
    String getCurrencyByAccountNumber(String accountNumber);
}
