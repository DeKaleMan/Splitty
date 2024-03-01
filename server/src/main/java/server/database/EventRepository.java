package server.database;

import commons.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
