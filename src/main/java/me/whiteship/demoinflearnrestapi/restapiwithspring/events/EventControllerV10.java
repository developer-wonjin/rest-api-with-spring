package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/api/v10/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventControllerV10 {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
//            return ResponseEntity.badRequest().build();
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();

        Event newEvent = eventRepository.save(event);


        WebMvcLinkBuilder selfLinkBuilder = linkTo(
                EventControllerV10.class
        );
        URI createUri = selfLinkBuilder.slash(newEvent.getId()).toUri();

        EntityModel<Event> eventEntityModel = EntityModel.of(
                newEvent,
                selfLinkBuilder.withRel("query-events"),
                selfLinkBuilder.slash(newEvent.getId()).withSelfRel(),
                selfLinkBuilder.slash(newEvent.getId()).withRel("update-event")
        );
        return ResponseEntity.created(createUri).body(eventEntityModel);
    }
}
