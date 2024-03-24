package server.database;

import commons.Event;
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

    @Query("SELECT p FROM Participant p WHERE p.id.event.id = :eventId")
    List<Participant> findByEventId(int eventId);


    @Query("SELECT e FROM Event e JOIN Participant p on e.id = p.id.event.id WHERE p.id.uuid = ?1")
    List<Event> findEventsByParticipant(String uuid);
}