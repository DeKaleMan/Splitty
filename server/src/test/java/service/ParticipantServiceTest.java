package service;

import commons.Event;
import commons.Participant;
import commons.dto.ParticipantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestEventRepository;
import server.api.TestParticipantRepository;
import server.service.ParticipantService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantServiceTest {

    private ParticipantService participantService;
    private TestParticipantRepository testParticipantRepository;
    private TestEventRepository testEventRepository;

    @BeforeEach
    void setUp() {
        testParticipantRepository = new TestParticipantRepository();
        testEventRepository = new TestEventRepository();
        participantService = new ParticipantService(testParticipantRepository, testEventRepository);
    }

    @Test
    void getAllParticipants() {
        Participant participant = new Participant();
        testParticipantRepository.participants.add(participant);

        List<Participant> participants = participantService.getAllParticipants();
        assertFalse(participants.isEmpty());
        assertEquals(1, participants.size());
    }

    @Test
    void getParticipantByIdWithNoEvent() {
        assertNull(participantService.getParticipantById(UUID.randomUUID().toString(), 1));
    }

    @Test
    void getParticipantByIdWithEventButNoParticipant() {
        Event event = new Event();
        testEventRepository.events.add(event);
        assertNull(participantService.getParticipantById(UUID.randomUUID().toString(), event.getId()));
    }

    @Test
    void getParticipantByIdSuccess() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.events.add(event);

        Participant participant = new Participant();
        participant.setUuid(UUID.randomUUID().toString());
        participant.setEvent(event);
        testParticipantRepository.participants.add(participant);

        assertNotNull(participantService.getParticipantById(participant.getId().getUuid(), event.getId()));
    }

    @Test
    void createParticipantWithNoEvent() {
        ParticipantDTO dto = new ParticipantDTO();
        dto.setEventInviteCode("INVITE_CODE");
        assertNull(participantService.createParticipant(dto));
    }

    @Test
    void createParticipantWithExistingParticipant() {
        Event event = new Event();
        event.setInviteCode("INVITE_CODE");
        testEventRepository.events.add(event);

        ParticipantDTO dto = new ParticipantDTO();
        dto.setUuid(UUID.randomUUID().toString());
        dto.setEventInviteCode("INVITE_CODE");

        Participant participant = new Participant();
        participant.setUuid(dto.getUuid());
        participant.setEvent(event);
        testParticipantRepository.participants.add(participant);

        assertNull(participantService.createParticipant(dto));
    }

    @Test
    void createParticipantSuccess() {
        Event event = new Event();
        event.setInviteCode("INVITE_CODE");
        testEventRepository.events.add(event);

        ParticipantDTO dto = new ParticipantDTO();
        dto.setUuid(UUID.randomUUID().toString());
        dto.setEventInviteCode("INVITE_CODE");
        dto.setName("Yavor Pachedjiev");

        assertNotNull(participantService.createParticipant(dto));
    }

    @Test
    void updateParticipantWithNoEvent() {
        assertNull(participantService.updateParticipant(UUID.randomUUID().toString(),
                1, new ParticipantDTO()));
    }

    @Test
    void updateParticipantWithNoExistingParticipant() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.events.add(event);

        assertNull(participantService.updateParticipant(UUID.randomUUID().toString(),
                event.getId(), new ParticipantDTO()));
    }

    @Test
    void updateParticipantSuccess() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.events.add(event);

        Participant participant = new Participant();
        participant.setUuid(UUID.randomUUID().toString());
        participant.setEvent(event);
        testParticipantRepository.participants.add(participant);

        ParticipantDTO dto = new ParticipantDTO();
        dto.setName("Updated Name");

        assertNotNull(participantService.updateParticipant(participant.getId().getUuid(), event.getId(), dto));
    }

    @Test
    void deleteParticipantWithNoEvent() {
        assertNull(participantService.deleteParticipant(UUID.randomUUID().toString(), 1));
    }

    @Test
    void deleteParticipantWithNoExistingParticipant() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.events.add(event);

        assertNull(participantService.deleteParticipant(UUID.randomUUID().toString(), event.getId()));
    }

    @Test
    void deleteParticipantSuccess() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.events.add(event);

        Participant participant = new Participant();
        participant.setUuid(UUID.randomUUID().toString());
        participant.setEvent(event);
        testParticipantRepository.participants.add(participant);

        assertNotNull(participantService.deleteParticipant(participant.getId().getUuid(), event.getId()));
    }

    @Test
    void getParticipantsByEventId() {
        Event event = new Event();
        event.setId(1);
        testEventRepository.events.add(event);

        Participant participant = new Participant();
        participant.setEvent(event);
        testParticipantRepository.participants.add(participant);

        List<Participant> participants = participantService.getByEvent(event.getId());
        assertFalse(participants.isEmpty());
        assertEquals(1, participants.size());
    }

    @Test
    void getEventsByParticipant() {
        Participant participant = new Participant();
        participant.setUuid(UUID.randomUUID().toString());
        Event event = new Event();
        participant.setEvent(event);

        testParticipantRepository.participants.add(participant);

        List<Event> events = participantService.getEventsByParticipant(participant.getUuid());
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
    }
}
