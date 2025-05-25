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
}
