package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorResource extends RepresentationModel<ErrorResource> {
    private final List<String> errors;

    public ErrorResource(List<String> errors) {
        this.errors = errors;

    }
}