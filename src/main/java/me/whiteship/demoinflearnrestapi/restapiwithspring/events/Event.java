package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)  @Builder.Default
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }

        if (this.location == null || this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }

    }
}
