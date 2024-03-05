package server.database;

import commons.PaymentParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PaymentParticipantRepository extends JpaRepository<PaymentParticipant, Long> {

    // Find all payments made by a specific payer using their email
    List<PaymentParticipant> findByPayerEmail(String payerEmail);

    // Find all payments received by a specific payee using their email
    List<PaymentParticipant> findByPayeeEmail(String payeeEmail);

    // Sum of all payments made by a specific payer
    @Query("SELECT SUM(pp.amount) FROM PaymentParticipant pp WHERE pp.payer.email = :payerEmail")
    Double sumPaymentsByPayerEmail(@Param("payerEmail") String payerEmail);

    // Sum of all payments received by a specific payee
    @Query("SELECT SUM(pp.amount) FROM PaymentParticipant pp WHERE pp.payee.email = :payeeEmail")
    Double sumPaymentsByPayeeEmail(@Param("payeeEmail") String payeeEmail);
}