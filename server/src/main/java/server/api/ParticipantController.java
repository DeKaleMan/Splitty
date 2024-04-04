package server.api;

import commons.Event;
import commons.Participant;
import commons.ParticipantId;
import commons.dto.ParticipantDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    private Map<Object, Consumer<Participant>> updateListeners = new HashMap<>();
    private Map<Object, Consumer<Participant>> deletionListeners = new HashMap<>();
    @Autowired
    public ParticipantController(ParticipantRepository participantRepository, EventRepository eventRepository) {
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping(path = {"/all"})
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<Participant>> getUpdates(){
        return getResponseEntityDeferredResult(updateListeners);
    }

    private DeferredResult<ResponseEntity<Participant>> getResponseEntityDeferredResult(
        Map<Object, Consumer<Participant>> listeners) {
        var noContent = ResponseEntity.noContent().build();
        var res = new DeferredResult<ResponseEntity<Participant>>(5000L, noContent);

        Object key = new Object();
        listeners.put(key, (p) -> {
            res.setResult(ResponseEntity.ok(p));
        });

        res.onCompletion(() -> {
            listeners.remove(key);
        });

        return res;
    }

    @GetMapping("/deletions")
    public DeferredResult<ResponseEntity<Participant>> getDeletions(){
        return getResponseEntityDeferredResult(deletionListeners);
    }


    @GetMapping("/{uuid}/{eventId}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable String uuid, @PathVariable int eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        Participant participant = participantRepository.findById(new ParticipantId(uuid, event));
        if (participant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participant);
    }

    @PostMapping
    public ResponseEntity<Participant> createParticipant(@RequestBody ParticipantDTO participantDTO) {
        Event event = eventRepository.findByInviteCode(participantDTO.getEventInviteCode());
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        // If he already exists?
        Participant existingParticipant = participantRepository.findById(
                new ParticipantId(participantDTO.getUuid(), event));
        if (existingParticipant != null) {
            return ResponseEntity.badRequest().build();
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

        Participant savedParticipant = participantRepository.save(participant);

        updateListeners.forEach((k, l) -> {l.accept(savedParticipant);});

        return ResponseEntity.ok(savedParticipant);
    }

    @PutMapping("/{uuid}/{eventId}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable String uuid,
                                                         @PathVariable int eventId,
                                                         @RequestBody ParticipantDTO participantDTO) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        Participant existingParticipant = participantRepository.findById(new ParticipantId(uuid, event));
        if (existingParticipant == null) {
            return ResponseEntity.notFound().build();
        }

        existingParticipant.setName(participantDTO.getName());
        existingParticipant.setBalance(participantDTO.getBalance());
        existingParticipant.setIBan(participantDTO.getiBan());
        existingParticipant.setBIC(participantDTO.getbIC());
        existingParticipant.setEmail(participantDTO.getEmail());
        existingParticipant.setAccountHolder(participantDTO.getAccountHolder());
        existingParticipant.setUuid(participantDTO.getUuid());

        Participant updatedParticipant = participantRepository.save(existingParticipant);

        updateListeners.forEach((k, l) -> {l.accept(updatedParticipant);});

        return ResponseEntity.ok(updatedParticipant);
    }

    @DeleteMapping("/{uuid}/{eventId}")
    public ResponseEntity<Participant> deleteParticipant(@PathVariable String uuid, @PathVariable int eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        Participant participant = participantRepository.findById(new ParticipantId(uuid, event));
        if (participant == null) {
            return ResponseEntity.notFound().build();
        }
        participantRepository.delete(participant);

        deletionListeners.forEach((k, l) -> {l.accept(participant);});

        return ResponseEntity.ok(participant);
    }

    @GetMapping("{uuid}/events")
    public ResponseEntity<List<Event>> getEventsByParticipant(@PathVariable String uuid) {
        List<Event> events = participantRepository.findEventsByParticipant(uuid);
        return ResponseEntity.ok(events);

    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Participant>> getByEvent(@RequestParam(name = "eventID") int eventID){
        List<Participant> p = participantRepository.findByEventId(eventID);
        return ResponseEntity.ok(p);
    }

}