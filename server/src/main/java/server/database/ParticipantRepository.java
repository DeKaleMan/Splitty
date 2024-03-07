package server.database;

import commons.Participant;
import commons.ParticipantId;
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
    Participant findById(ParticipantId id);
}