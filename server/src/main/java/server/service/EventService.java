package server.service;

import commons.Event;
import commons.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 6;

    // apparently this uses "entropy sources" to generate random numbers such as mouse movements, keyboard presses, etc
    private final SecureRandom random = new SecureRandom();
    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    public Event getEventById(int id) {
        return eventRepository.findById(id).orElse(null);
    }


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event saveEvent(EventDTO inputEvent) {
        if (inputEvent == null || isNullOrEmpty(inputEvent.getName()) || inputEvent.getDate() == null
                || isNullOrEmpty(inputEvent.getOwner()) || inputEvent.getDescription() == null) {
            return null;
        }
        String inviteCode = generateUniqueCode();
        Event newEvent = new Event(inputEvent.getName(), inputEvent.getDate(),
                inputEvent.getOwner(), inputEvent.getDescription());
        newEvent.setInviteCode(inviteCode);
        return eventRepository.save(newEvent);
    }

    public Event removeEvent(Integer id) {
        Optional<Event> eventToDelete = eventRepository.findById(id);
        eventToDelete.ifPresent(event -> eventRepository.deleteById(id));
        return eventToDelete.orElse(null);
    }

    public Event updateEventName(Integer eventId, String newName) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        optionalEvent.ifPresent(event -> {
            event.setName(newName);
            eventRepository.save(event);
        });
        return optionalEvent.orElse(null);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (eventRepository.findByInviteCode(code)!=null);
        return code;
    }

    private String generateCode() {
        StringBuilder builder = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            builder.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return builder.toString();
    }


    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
