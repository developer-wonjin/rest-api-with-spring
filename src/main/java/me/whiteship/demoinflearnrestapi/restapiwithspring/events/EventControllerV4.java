package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/api/v4/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventControllerV4 {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class); // Mocking에 사용된 event 객체와 다른 객체다
        Event newEvent = eventRepository.save(event); // eventRepositoryMockBean은 null을 리턴하게 된다.
        URI createdUri = linkTo(
                            EventControllerV4.class
                        ).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
    }
}
