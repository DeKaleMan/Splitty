package server.service;

import commons.Event;
import commons.Participant;
import commons.ParticipantId;
import commons.dto.ParticipantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.List;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository, EventRepository eventRepository) {
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public Participant getParticipantById(String uuid, int eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            return null;
        }
        return participantRepository.findById(new ParticipantId(uuid, event));
    }

    public Participant createParticipant(ParticipantDTO participantDTO) {
        Event event = eventRepository.findByInviteCode(participantDTO.getEventInviteCode());
        if (event == null) {
            return null;
        }

        Participant existingParticipant = participantRepository.findById(
                new ParticipantId(participantDTO.getUuid(), event));
        if (existingParticipant != null) {
            return null;
        }

        Participant participant = new Participant(
                participantDTO.getName(),
                participantDTO.getBalance(),
                participantDTO.getiBan(),
                participantDTO.getbIC(),
                participantDTO.getEmail(),
                participantDTO.getAccountHolder(),
                participantDTO.getUuid(),
                event
        );
        participant.setGhost(participantDTO.isGhost());

        return participantRepository.save(participant);
    }

    public Participant updateParticipant(String uuid, int eventId, ParticipantDTO participantDTO) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            return null;
        }
        Participant existingParticipant = participantRepository.findById(new ParticipantId(uuid, event));
        if (existingParticipant == null) {
            return null;
        }

        existingParticipant.setName(participantDTO.getName());
        existingParticipant.setBalance(participantDTO.getBalance());
        existingParticipant.setIBan(participantDTO.getiBan());
        existingParticipant.setBIC(participantDTO.getbIC());
        existingParticipant.setEmail(participantDTO.getEmail());
        existingParticipant.setAccountHolder(participantDTO.getAccountHolder());
        // No UUID update as it's the identifier
        return participantRepository.save(existingParticipant);
    }

    public Participant deleteParticipant(String uuid, int eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            return null;
        }
        Participant participant = participantRepository.findById(new ParticipantId(uuid, event));
        if (participant == null) {
            return null;
        }
        participantRepository.delete(participant);
        return participant;
    }

    public List<Event> getEventsByParticipant(String uuid) {
        return participantRepository.findEventsByParticipant(uuid);
    }

    public List<Participant> getByEvent(int eventID) {
        return participantRepository.findByEventId(eventID);
    }
}
