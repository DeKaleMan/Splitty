package server.api;

import commons.Participant;
import org.springframework.http.HttpStatus;
import server.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantService.findAllParticipants();
    }

    @GetMapping("/{email}")
    public ResponseEntity<Participant> getParticipantByEmail(@PathVariable String email) {
        Optional<Participant> participant = participantService.findParticipantByEmail(email);
        if (participant.isPresent()) {
            return new ResponseEntity<>(participant.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}