package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
