package server.api;

import commons.Event;
import commons.Participant;
import commons.dto.ParticipantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import server.service.ParticipantService;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ParticipantControllerTest {

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantController participantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        participantController = new ParticipantController(participantService);
    }

    @Test
    void getAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant());
        when(participantService.getAllParticipants()).thenReturn(participants);

        List<Participant> result = participantController.getAllParticipants();
        assertEquals(1, result.size());
    }

    @Test
    void getParticipantByIdFound() {
        Participant participant = new Participant();
        when(participantService.getParticipantById(any(String.class), any(Integer.class))).thenReturn(participant);


        ResponseEntity<Participant> result = participantController.getParticipantById("uuid", 1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getParticipantByIdNotFound() {
        when(participantService.getParticipantById(any(String.class), any(Integer.class))).thenReturn(null);
        ResponseEntity<Participant> result = participantController.getParticipantById("uuid", 1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void createParticipantSuccessful() {
        Participant participant = new Participant();
        when(participantService.createParticipant(any(ParticipantDTO.class))).thenReturn(participant);
        ResponseEntity<Participant> result = participantController.createParticipant(new ParticipantDTO());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void createParticipantNotFound() {
        when(participantService.createParticipant(any(ParticipantDTO.class))).thenReturn(null);
        ResponseEntity<Participant> result = participantController.createParticipant(new ParticipantDTO());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void updateParticipantSuccessful() {
        Participant participant = new Participant();


        when(participantService.updateParticipant(any(String.class),
                any(Integer.class),
                any(ParticipantDTO.class)))
                .thenReturn(participant);
        ResponseEntity<Participant> result = participantController.updateParticipant("uuid", 1, new ParticipantDTO());

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateParticipantNotFound() {
        when(participantService.updateParticipant(any(String.class),
                any(Integer.class),
                any(ParticipantDTO.class)))
                .thenReturn(null);
        ResponseEntity<Participant> result = participantController.updateParticipant("uuid", 1, new ParticipantDTO());

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void deleteParticipantSuccessful() {
        Participant participant = new Participant();
        when(participantService.deleteParticipant(any(String.class), any(Integer.class))).thenReturn(participant);

        ResponseEntity<Participant> result = participantController.deleteParticipant("uuid", 1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteParticipantNotFound() {
        when(participantService.deleteParticipant(any(String.class), any(Integer.class))).thenReturn(null);


        ResponseEntity<Participant> result = participantController.deleteParticipant("uuid", 1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void getEventsByParticipantFound() {
        List<Event> events = new ArrayList<>();

        events.add(new Event());
        when(participantService.getEventsByParticipant(any(String.class))).thenReturn(events);



        ResponseEntity<List<Event>> result = participantController.getEventsByParticipant("uuid");
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getEventsByParticipantNotFound() {

        when(participantService.getEventsByParticipant(any(String.class))).thenReturn(new ArrayList<>());

        ResponseEntity<List<Event>> result = participantController.getEventsByParticipant("uuid");
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getByEventFound() {
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant());

        when(participantService.getByEvent(any(Integer.class))).thenReturn(participants);
        ResponseEntity<List<Participant>> result = participantController.getByEvent(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getByEventNotFound() {
        when(participantService.getByEvent(any(Integer.class))).thenReturn(new ArrayList<>());

        ResponseEntity<List<Participant>> result = participantController.getByEvent(1);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testGetUpdates() {
        DeferredResult<ResponseEntity<Participant>> deferredResult = participantController.getUpdates();
        Participant mockParticipant = new Participant();
        participantController.notifyUpdateListeners(mockParticipant);

        ResponseEntity<Participant> responseEntity = (ResponseEntity<Participant>) deferredResult.getResult();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockParticipant, responseEntity.getBody());
    }

    @Test
    void testGetDeletions() {
        DeferredResult<ResponseEntity<Participant>> deferredResult = participantController.getDeletions();
        Participant mockParticipant = new Participant();

        participantController.notifyDeletionListeners(mockParticipant);


        ResponseEntity<Participant> responseEntity = (ResponseEntity<Participant>) deferredResult.getResult();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(mockParticipant, responseEntity.getBody());
    }
}
