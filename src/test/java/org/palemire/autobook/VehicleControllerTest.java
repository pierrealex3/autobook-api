package org.palemire.autobook;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.palemire.autobook.appointment.AppointmentDto;
import org.palemire.autobook.appointment.AppointmentEntity;
import org.palemire.autobook.appointment.AppointmentNoteDto;
import org.palemire.autobook.appointment.AppointmentWorkItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("itest")
@AutoConfigureMockMvc
@Testcontainers
class VehicleControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager entityManager;

    @Test
    @Sql(scripts = "/sql/insertUserAndVehicle.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void appointmentWithNotes_createAppointment_expectedAppointmentOnReturn() throws Exception {

        var appointmentTitle = "Warranty service";
        var appointmentNote = "once a year";
        var appointmentDate = LocalDate.of(2025, Month.DECEMBER, 17);
        var appointmentTime = LocalTime.of(9, 0);
        var appointmentNote1 = "Check the tires soon";
        var appointmentNote2 = "Free tire storage for the winter";
        var appointmentWorkItem1Title = "Oil change";

        var dto = AppointmentDto.builder()
                .title(appointmentTitle)
                .note(appointmentNote)
                .date(appointmentDate)
                .time(appointmentTime)
                .appointmentWorkItems(
                        List.of(
                                AppointmentWorkItemDto.builder()
                                        .title(appointmentWorkItem1Title)
                                        .build()
                        )
                )
                .appointmentNotes(
                        List.of(
                                AppointmentNoteDto.builder().note(appointmentNote1).build(),
                                AppointmentNoteDto.builder().note(appointmentNote2).build()
                        )
                ).build();

        mockMvc.perform(
                post(Constants.AUTOBOOK_API_ROOT + "/vehicles/1/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated());

        // verify the *targeted i.e. not the whole* state of the database via the entity model

        List<AppointmentEntity> appointmentEntityList = entityManager.createQuery("""
        SELECT ae FROM AppointmentEntity ae
        LEFT JOIN FETCH ae.appointmentNotes an
        LEFT JOIN FETCH ae.appointmentWorkItems awi
        """, AppointmentEntity.class).getResultList();

        assertThat(appointmentEntityList).hasSize(1);
        var appointmentEntity = appointmentEntityList.get(0);
        assertThat(appointmentEntity.getTitle()).isEqualTo(appointmentTitle);
        assertThat(appointmentEntity.getNote()).isEqualTo(appointmentNote);
        assertThat(appointmentEntity.getDate()).isEqualTo(appointmentDate);
        assertThat(appointmentEntity.getTime()).isEqualTo(appointmentTime);
        assertThat(appointmentEntity.getAppointmentNotes()).extracting("note").containsExactlyInAnyOrderElementsOf(List.of(appointmentNote1, appointmentNote2));
        assertThat(appointmentEntity.getAppointmentWorkItems()).extracting("title").containsExactlyInAnyOrderElementsOf(List.of(appointmentWorkItem1Title));

    }


}
