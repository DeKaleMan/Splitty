package server.database;

import commons.Participant;
import commons.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // Find participants by name
    List<Participant> findByName(String name);

    // Find a single participant by id
    Participant findById(ParticipantId id);
}