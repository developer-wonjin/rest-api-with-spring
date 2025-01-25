package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerV5Test {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("application.yml에 정의한 fail-on-unknown-properties: true 으로 인해 400에러 터짐" +
            "org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Unrecognized field \"id\"")
    @Test
    void createEvent() throws Exception {
        Event event = Event.builder()
                .id(100L) // 입력되어선 안되는 필드
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
                .free(true) // 입력되어선 안되는 필드
                .offline(false) // 입력되어선 안되는 필드
                .eventStatus(EventStatus.PUBLISHED)
                .build();
        System.out.println("event = " + event);

        mockMvc.perform(post("/api/v5/events")
                        .contentType(MediaType.APPLICATION_JSON)// 요청의 타입
                        .accept(MediaTypes.HAL_JSON) // 응답의 타입
                        .content(objectMapper.writeValueAsString(event)) // 응답의 내용
                )
                .andDo(print())
                .andExpect(status().isBadRequest())// application.yml에 정의한 fail-on-unknown-properties: true 으로 인해
        ;
    }
}