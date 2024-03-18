package server.database;

import commons.Participant;
import commons.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // Find participants by name
    List<Participant> findByName(String name);

    // Find a single participant by id
    Participant findById(ParticipantId id);

    @Query("SELECT p FROM Participant p WHERE p.id.event.id = ?1")
    List<Participant> findByEventId(int eventId);
}