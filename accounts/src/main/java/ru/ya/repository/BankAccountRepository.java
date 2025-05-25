package ru.ya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ya.model.BankAccount;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    List<BankAccount> findAllById(int id);

/*    @Query(value = """
            DELETE 
            FROM accounts.bank_accounts
            WHERE id = :id
            """, nativeQuery = true)
    void deleteBankAccount(int id);*/
}
