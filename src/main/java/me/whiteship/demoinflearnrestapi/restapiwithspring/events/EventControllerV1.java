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

@Controller
@RequestMapping(value = "/api/v1/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventControllerV1 {

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        URI location = linkTo(
                            EventControllerV1.class
                        ).slash("{id}").toUri();
        event.setId(10L);
        return ResponseEntity.created(location).body(event);
    }

}
