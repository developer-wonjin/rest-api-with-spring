package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerV7Test {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createEvent_badRequest_emptyInput() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/v7/events")
                        .contentType(MediaType.APPLICATION_JSON)// 요청의 타입
                        .accept(MediaTypes.HAL_JSON) // 응답의 타입
                        .content(objectMapper.writeValueAsString(eventDto)) // 응답의 내용
                )
                .andExpect(status().isBadRequest())
        ;
    }

    @DisplayName("Bad Request처리")
    @Test
    void createEvent_badRequest_wrongInput() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("A")
                .description("B")
                .beginEnrollmentDateTime(LocalDateTime.of(2010, 11, 21, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2010, 11, 20, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2010, 11, 24, 14, 20))
                .endEventDateTime(LocalDateTime.of(2010, 11, 23, 14, 21))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("seoul")
                .build();

        mockMvc.perform(post("/api/v7/events")
                        .contentType(MediaType.APPLICATION_JSON)// 요청의 타입
                        .accept(MediaTypes.HAL_JSON) // 응답의 타입
                        .content(objectMapper.writeValueAsString(eventDto)) // 응답의 내
                )
                .andExpect(status().isBadRequest())
        ;
    }
}