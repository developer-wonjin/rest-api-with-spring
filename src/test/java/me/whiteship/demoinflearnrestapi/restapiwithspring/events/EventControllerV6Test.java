package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
@AutoConfigureMockMvc // MockMvc모의객체를 스프링컨테이너에 빈등록해주는 어노테이션
public class EventControllerV6Test {
    @Autowired
    MockMvc mockMvc;// 스프링컨테이너에 등록된 빈을 주입받는다

    @Autowired
    ObjectMapper objectMapper;

    // repository Mock객체 제거. 실제 repository 빈객체 사용하게끔 유도
    
    @Test
    void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
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
                .build();

        mockMvc.perform(post("/api/v6/events")
                        .contentType(MediaType.APPLICATION_JSON)// 요청의 타입
                        .accept(MediaTypes.HAL_JSON) // 응답의 타입
                        .content(objectMapper.writeValueAsString(eventDto)) // 응답의 내용
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100L)))   // eventDto 덕분에 잘 통과된다.
                .andExpect(jsonPath("free").value(Matchers.not(true))) // eventDto 덕분에 잘 통과된다.
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name())) // eventDto 덕분에 잘 통과된다.
        ;
    }

    @Test
    void createEvent_badRequest() throws Exception {
        Event event = Event.builder()
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
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/v6/events")
                        .contentType(MediaType.APPLICATION_JSON)// 요청의 타입
                        .accept(MediaTypes.HAL_JSON) // 응답의 타입
                        .content(objectMapper.writeValueAsString(event)) // 응답의 내용
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }
}