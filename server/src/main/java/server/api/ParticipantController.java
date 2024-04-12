package server.api;

import commons.Event;
import commons.Participant;
import commons.dto.ParticipantDTO;
import server.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    protected Map<Object, Consumer<Participant>> updateListeners = new ConcurrentHashMap<>();
    protected Map<Object, Consumer<Participant>> deletionListeners = new ConcurrentHashMap<>();

    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/all")
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<Participant>> getUpdates() {
        return getResponseEntityDeferredResult(updateListeners);
    }

    @GetMapping("/deletions")
    public DeferredResult<ResponseEntity<Participant>> getDeletions() {
        return getResponseEntityDeferredResult(deletionListeners);
    }

    @GetMapping("/{uuid}/{eventId}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable String uuid, @PathVariable int eventId) {
        Participant participant = participantService.getParticipantById(uuid, eventId);
        if (participant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participant);
    }

    @PostMapping
    public ResponseEntity<Participant> createParticipant(@RequestBody ParticipantDTO participantDTO) {
        Participant participant = participantService.createParticipant(participantDTO);
        if (participant == null) {
            return ResponseEntity.notFound().build();
        }
        notifyUpdateListeners(participant);
        return ResponseEntity.ok(participant);
    }

    @PutMapping("/{uuid}/{eventId}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable String uuid,
                                                         @PathVariable int eventId,
                                                         @RequestBody ParticipantDTO participantDTO) {
        Participant participant = participantService.updateParticipant(uuid, eventId, participantDTO);
        if (participant == null) {
            return ResponseEntity.notFound().build();
        }
        notifyUpdateListeners(participant);
        return ResponseEntity.ok(participant);
    }

    @DeleteMapping("/{uuid}/{eventId}")
    public ResponseEntity<Participant> deleteParticipant(@PathVariable String uuid, @PathVariable int eventId) {
        Participant participant = participantService.deleteParticipant(uuid, eventId);
        if (participant == null) {
            return ResponseEntity.notFound().build();
        }
        notifyDeletionListeners(participant);
        return ResponseEntity.ok(participant);
    }

    @GetMapping("{uuid}/events")
    public ResponseEntity<List<Event>> getEventsByParticipant(@PathVariable String uuid) {
        List<Event> events = participantService.getEventsByParticipant(uuid);
        return ResponseEntity.ok(events);
    }

    @GetMapping("")
    public ResponseEntity<List<Participant>> getByEvent(@RequestParam(name = "eventID") int eventID) {
        List<Participant> participants = participantService.getByEvent(eventID);
        if (participants.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participants);
    }

    private DeferredResult<ResponseEntity<Participant>> getResponseEntityDeferredResult(
            Map<Object, Consumer<Participant>> listeners) {
        var noContent = ResponseEntity.noContent().build();
        var result = new DeferredResult<ResponseEntity<Participant>>(5000L, noContent);

        Object key = new Object();
        listeners.put(key, p -> result.setResult(ResponseEntity.ok(p)));

        result.onCompletion(() -> listeners.remove(key));

        return result;
    }

    protected void notifyUpdateListeners(Participant participant) {
        updateListeners.forEach((key, listener) -> listener.accept(participant));
    }

    protected void notifyDeletionListeners(Participant participant) {
        deletionListeners.forEach((key, listener) -> listener.accept(participant));
    }
}
