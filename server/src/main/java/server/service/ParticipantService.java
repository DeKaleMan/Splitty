package server.service;

import commons.Participant;
import server.database.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<Participant> findAllParticipants() {
        return participantRepository.findAll();
    }

    public Optional<Participant> findParticipantByEmail(String email) {
        return Optional.ofNullable(participantRepository.findByEmail(email));
    }

    public Participant saveParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    public void deleteParticipantByEmail(String email) {
        participantRepository.deleteByEmail(email);
    }
}