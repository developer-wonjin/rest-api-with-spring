package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/v0/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventControllerV0 {

    @PostMapping
    public ResponseEntity createEvent() {
        URI location = linkTo(
                            methodOn(EventControllerV0.class).createEvent()
                        ).slash("{id}").toUri();
        return ResponseEntity.created(location).build();
    }

}
