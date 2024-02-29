package server.database;

import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // Find participants by name
    List<Participant> findByName(String name);

    // Find a single participant by email, assuming emails are unique
    Participant findByEmail(String email);

    // Find participants by account holder name using a custom query
    @Query("SELECT p FROM Participant p WHERE p.accountHolder = :accountHolder")
    List<Participant> findByAccountHolder(@Param("accountHolder") String accountHolder);

    // Delete a participant by email
    void deleteByEmail(String email);
}