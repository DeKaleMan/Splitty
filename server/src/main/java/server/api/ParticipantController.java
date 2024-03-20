package server.api;

import commons.Event;
import commons.Participant;
import commons.ParticipantId;
import commons.dto.ParticipantDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.List;


@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    @Autowired
    public ParticipantController(ParticipantRepository participantRepository, EventRepository eventRepository) {
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }


    @GetMapping("/{email}/{eventId}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable String email, @PathVariable int eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        Participant participant = participantRepository.findById(new ParticipantId(email, event));
        if (participant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participant);
    }

    @PostMapping
    public ResponseEntity<Participant> createParticipant(@RequestBody ParticipantDTO participantDTO) {
        Event event = eventRepository.findEventById(participantDTO.getEventId());
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        Participant participant = new Participant(participantDTO.getName(),
                participantDTO.getBalance(),
                participantDTO.getiBan(),
                participantDTO.getbIC(),
                participantDTO.getAccountHolder(),
                participantDTO.getEmail(),
                event);

        Participant savedParticipant = participantRepository.save(participant);
        return ResponseEntity.ok(savedParticipant);
    }

    @PutMapping("/{email}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable String email,
                                                         @RequestBody ParticipantDTO participantDTO) {
        Event event = eventRepository.findEventById(participantDTO.getEventId());
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        Participant existingParticipant = participantRepository.findById(new ParticipantId(email, event));
        if (existingParticipant == null) {
            return ResponseEntity.notFound().build();
        }
        existingParticipant.setName(participantDTO.getName());
        existingParticipant.setBalance(participantDTO.getBalance());
        existingParticipant.setIBan(participantDTO.getiBan());
        existingParticipant.setBIC(participantDTO.getbIC());
        existingParticipant.setAccountHolder(participantDTO.getAccountHolder());

        Participant updatedParticipant = participantRepository.save(existingParticipant);
        return ResponseEntity.ok(updatedParticipant);
    }


    @GetMapping("/{eventID}")
    public ResponseEntity<List<Participant>> getByEvent(@PathVariable int eventID){
        List<Participant> p = participantRepository.findByEventId(eventID);
        return ResponseEntity.ok(p);
    }

}