package server.api;

import commons.Participant;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @GetMapping("/{email}")
    public ResponseEntity<Participant> getParticipantByEmail(@PathVariable String email) {
        Optional<Participant> participant = Optional.ofNullable(participantRepository.findByEmail(email));;
        if (participant.isPresent()) {
            return new ResponseEntity<>(participant.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}