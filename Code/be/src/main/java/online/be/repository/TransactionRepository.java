package online.be.repository;

import online.be.entity.Transaction;
import online.be.enums.TransactionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Transaction findByTransactionID(UUID id);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.from.walletID = :walletId " +
            "OR t.to.walletID = :walletId")
    List<Transaction> findTransactionByFrom_IdOrTo_Id(long walletId);

    List<Transaction> findTransactionByTransactionType(TransactionEnum status);
}
