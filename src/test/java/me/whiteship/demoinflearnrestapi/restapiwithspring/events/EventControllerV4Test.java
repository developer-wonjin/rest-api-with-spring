package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(EventControllerV4.class)
public class EventControllerV4Test {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EventRepository eventRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createEvent() throws Exception {
        // given
        Event event = Event.builder()
                .id(100L) // 입력되어선 안되는 필드 => 테스트실패를 초래함
                .name("A")
                .description("B")
                .beginEnrollmentDateTime(LocalDateTime.of(2010, 11, 20, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2010, 11, 21, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2010, 11, 23, 14, 20))
                .endEventDateTime(LocalDateTime.of(2010, 11, 24, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("seoul")
                .free(true) // 입력되어선 안되는 필드 => 테스트실패를 초래함
                .offline(false) // 입력되어선 안되는 필드 => 테스트실패를 초래함
                .build();
        System.out.println("event: " + event);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/v4/events")
                        .contentType(MediaType.APPLICATION_JSON)// 요청의 타입
                        .accept(MediaTypes.HAL_JSON) // 응답의 타입
                        .content(objectMapper.writeValueAsString(event)) // 응답의 내용
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100L)))
                .andExpect(jsonPath("free").value(Matchers.not(true))) // 테스트실패
                .andExpect(jsonPath("offline").value(Matchers.not(false))) // 테스트실패
        ;
    }
}