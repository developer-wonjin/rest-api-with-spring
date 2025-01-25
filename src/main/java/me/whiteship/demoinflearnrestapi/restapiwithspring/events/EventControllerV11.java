package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/api/v11/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventControllerV11 {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            ErrorResource errorResource = new ErrorResource(
                    errors.getFieldErrors().stream()
                            .map(FieldError::getDefaultMessage)
                            .collect(Collectors.toList())
            );
            errorResource.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
            return ResponseEntity.badRequest().body(errorResource);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();

        Event newEvent = eventRepository.save(event);


        WebMvcLinkBuilder selfLinkBuilder = linkTo(
                EventControllerV11.class
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
