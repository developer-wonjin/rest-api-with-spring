package me.whiteship.demoinflearnrestapi.restapiwithspring.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 
@WebMvcTest(EventControllerV1.class) // 모든 컨트롤러가 Bean생성되지 않고 테스트하고 싶은 부분만 명시한다
public class EventControllerV1Test {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createEvent() throws Exception {
        // given
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
                .build();

        mockMvc.perform(post("/api/v1/events")
                        // 요청의 타입
                        .contentType(MediaType.APPLICATION_JSON)
                        // 응답의 타입
                        .accept(MediaTypes.HAL_JSON)
                        // 응답의 내용
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
           ;
    }
}
