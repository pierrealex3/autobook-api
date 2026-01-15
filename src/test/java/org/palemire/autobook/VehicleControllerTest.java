package org.palemire.autobook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.palemire.autobook.appointment.AppointmentDto;
import org.palemire.autobook.appointment.AppointmentEntity;
import org.palemire.autobook.appointment.AppointmentNoteDto;
import org.palemire.autobook.appointment.AppointmentWorkItemDto;
import org.palemire.autobook.appointment.AppointmentWorkItemLaborDto;
import org.palemire.autobook.appointment.AppointmentWorkItemLaborEntity;
import org.palemire.autobook.appointment.AppointmentWorkItemPieceBuyDto;
import org.palemire.autobook.appointment.AppointmentWorkItemPieceEntity;
import org.palemire.autobook.appointment.PieceSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    /**
     * The natural flow: CREATE -> UPDATE -> GET Appointment has all been included in a single integration test, for the sake of simplicity.
     * @throws Exception
     */
    @Test
    @Sql(scripts = "/sql/insertUserAndVehicle.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void appointmentWithNotes_createThenUpdateThenGetAppointment_expectedDatabaseContentAndExpectedDtoReturned() throws Exception {

        /**
         * API call to CREATE the whole appointment resource
         */

        var appointmentTitle = "Warranty service";
        var appointmentNote = "once a year";
        var appointmentDate = LocalDate.of(2025, Month.DECEMBER, 17);
        var appointmentTime = LocalTime.of(9, 0);
        var appointmentNote1 = "Check the tires soon";
        var appointmentNote2 = "Free tire storage for the winter";
        var appointmentWorkItem1Title = "Oil change";

        var appointmentWorkItem1Labor1Title = "Empty old oil";
        var appointmentWorkItem1Labor1HoursWorked = new BigDecimal("0.50");
        var appointmentWorkItem1Labor1Cost = new BigDecimal("25.50");

        var appointmentWorkItem1Labor2Title = "Fill with new oil";
        var appointmentWorkItem1Labor2HoursWorked = new BigDecimal("0.75");
        var appointmentWorkItem1Labor2Cost = new BigDecimal("30.11");

        var appointmentWorkItem1Piece1Title = "Oil pints";
        var appointmentWorkItem1Piece1Cost = new BigDecimal("11.11");

        String appointmentWorkItem1Piece2Title = "Stronger Oil filter ";
        var appointmentWorkItem1Piece2Cost = new BigDecimal("110.11");

        Long appointmentWorkItem1Piece2PieceId = 1L;
        var appointmentWorkItem1Piece2PieceTitle = "Winter tires";
        var appointmentWorkItem1Piece2PieceCategory = "TIRE";

        var dtoForCreate = AppointmentDto.builder()
                .title(appointmentTitle)
                .note(appointmentNote)
                .date(appointmentDate)
                .time(appointmentTime)
                .appointmentWorkItems(
                        List.of(
                                AppointmentWorkItemDto.builder()
                                        .title(appointmentWorkItem1Title)
                                        .appointmentWorkItemsLabor(
                                                List.of(
                                                        AppointmentWorkItemLaborDto.builder()
                                                                .title(appointmentWorkItem1Labor1Title)
                                                                .hoursWorked(appointmentWorkItem1Labor1HoursWorked)
                                                                .cost(appointmentWorkItem1Labor1Cost)
                                                                .build(),
                                                        AppointmentWorkItemLaborDto.builder()
                                                                .title(appointmentWorkItem1Labor2Title)
                                                                .hoursWorked(appointmentWorkItem1Labor2HoursWorked)
                                                                .cost(appointmentWorkItem1Labor2Cost)
                                                                .build()
                                                )
                                        )
                                        .appointmentWorkItemsPiece(
                                                List.of(
                                                        AppointmentWorkItemPieceBuyDto.builder()
                                                                .title(appointmentWorkItem1Piece1Title)
                                                                .cost(appointmentWorkItem1Piece1Cost)
                                                                // piece is null
                                                                .build(),
                                                        AppointmentWorkItemPieceBuyDto.builder()
                                                                .title(appointmentWorkItem1Piece2Title)
                                                                .cost(appointmentWorkItem1Piece2Cost)
                                                                .piece(PieceSummaryDto.builder()
                                                                        .id(appointmentWorkItem1Piece2PieceId)
                                                                        .title(appointmentWorkItem1Piece2PieceTitle)
                                                                        .category(appointmentWorkItem1Piece2PieceCategory)
                                                                        .build()
                                                                )
                                                                .build()
                                                )
                                        )
                                        .build()
                        )
                )
                .appointmentNotes(
                        List.of(
                                AppointmentNoteDto.builder().note(appointmentNote1).build(),
                                AppointmentNoteDto.builder().note(appointmentNote2).build()
                        )
                ).build();

        MvcResult creationResult = mockMvc.perform(
                post(Constants.AUTOBOOK_API_ROOT + "/vehicles/1/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoForCreate))
                )
                .andExpect(status().isCreated())
                .andReturn();

        String xid = JsonPath.read(creationResult.getResponse().getContentAsString(), "$.xid");
        assertThat(xid).isNotBlank();

        // verify the *targeted i.e. not the whole* state of the database via the entity model

        List<AppointmentEntity> appointmentEntityList = fetchWholeAppointmentEntityModel();

        assertThat(appointmentEntityList).hasSize(1);
        var appointmentEntity = appointmentEntityList.get(0);
        assertThat(appointmentEntity.getTitle()).isEqualTo(appointmentTitle);
        assertThat(appointmentEntity.getNote()).isEqualTo(appointmentNote);
        assertThat(appointmentEntity.getDate()).isEqualTo(appointmentDate);
        assertThat(appointmentEntity.getTime()).isEqualTo(appointmentTime);
        assertThat(appointmentEntity.getAppointmentNotes()).extracting("note").containsExactlyInAnyOrderElementsOf(List.of(appointmentNote1, appointmentNote2));
        assertThat(appointmentEntity.getAppointmentWorkItems()).extracting("title").containsExactlyInAnyOrderElementsOf(List.of(appointmentWorkItem1Title));

        List<AppointmentWorkItemLaborEntity> appointmentWorkItemLaborEntities = appointmentEntity.getAppointmentWorkItems().stream().flatMap(awie -> awie.getAppointmentWorkItemsLabor().stream()).toList();

        assertThat(appointmentWorkItemLaborEntities)
                .hasSize(2)
                .allMatch(awile ->
                appointmentWorkItem1Labor1Title.equals(awile.getTitle()) &&
                        appointmentWorkItem1Labor1HoursWorked.compareTo(awile.getHoursWorked()) == 0 &&
                        appointmentWorkItem1Labor1Cost.compareTo(awile.getCost()) == 0
                        ||
                        appointmentWorkItem1Labor2Title.equals(awile.getTitle()) &&
                                appointmentWorkItem1Labor2HoursWorked.compareTo(awile.getHoursWorked()) == 0 &&
                                appointmentWorkItem1Labor2Cost.compareTo(awile.getCost()) == 0
        );

        List<AppointmentWorkItemPieceEntity> appointmentWorkItemPieceEntities = appointmentEntity.getAppointmentWorkItems().stream().flatMap(awie -> awie.getAppointmentWorkItemsPiece().stream()).toList();

        assertThat(appointmentWorkItemPieceEntities)
                .hasSize(2)
                .allMatch(awipe ->
                        appointmentWorkItem1Piece1Title.equals(awipe.getTitle()) &&
                                Objects.isNull(awipe.getPiece())
                ||
                                appointmentWorkItem1Piece2Title.equals(awipe.getTitle()) &&
                                        appointmentWorkItem1Piece2PieceId.equals(awipe.getPiece().getId())
        );


        /**
         * API call to UPDATE the whole appointment resource
          */

        var appointmentDateU = LocalDate.of(2025, Month.DECEMBER, 18);

        var appointmentWorkItem1Labor2TitleU = "Oil refill";
        var appointmentWorkItem1Labor2HoursWorkedU = new BigDecimal("0.79");
        var appointmentWorkItem1Labor2CostU = new BigDecimal("30.11");

        // one more item
        var appointmentWorkItem1Labor3TitleU = "Cleanup";
        var appointmentWorkItem1Labor3HoursWorkedU = new BigDecimal("0.48");
        var appointmentWorkItem1Labor3CostU = new BigDecimal("30.11");

        var dtoForUpdate = AppointmentDto.builder()
                .title(appointmentTitle)
                .note(appointmentNote)
                .date(appointmentDateU)  // Updated
                .time(appointmentTime)
                .appointmentWorkItems(
                        List.of(
                                AppointmentWorkItemDto.builder()
                                        .title(appointmentWorkItem1Title)
                                        .appointmentWorkItemsLabor(
                                                List.of(
                                                        AppointmentWorkItemLaborDto.builder()
                                                                .title(appointmentWorkItem1Labor1Title)
                                                                .hoursWorked(appointmentWorkItem1Labor1HoursWorked)
                                                                .cost(appointmentWorkItem1Labor1Cost)
                                                                .build(),
                                                        AppointmentWorkItemLaborDto.builder() // Updated
                                                                .title(appointmentWorkItem1Labor2TitleU)
                                                                .hoursWorked(appointmentWorkItem1Labor2HoursWorkedU)
                                                                .cost(appointmentWorkItem1Labor2CostU)
                                                                .build(),
                                                        AppointmentWorkItemLaborDto.builder() // Added
                                                                .title(appointmentWorkItem1Labor3TitleU)
                                                                .hoursWorked(appointmentWorkItem1Labor3HoursWorkedU)
                                                                .cost(appointmentWorkItem1Labor3CostU)
                                                                .build()
                                                )
                                        )
                                        .appointmentWorkItemsPiece(
                                                List.of(
                                                        AppointmentWorkItemPieceBuyDto.builder()
                                                                .title(appointmentWorkItem1Piece1Title)
                                                                .cost(appointmentWorkItem1Piece1Cost)
                                                                // piece is null
                                                                .build(),
                                                        AppointmentWorkItemPieceBuyDto.builder()
                                                                .title(appointmentWorkItem1Piece2Title)
                                                                .cost(appointmentWorkItem1Piece2Cost)
                                                                .piece(
                                                                        PieceSummaryDto.builder()
                                                                                .id(appointmentWorkItem1Piece2PieceId)
                                                                                .title(appointmentWorkItem1Piece2PieceTitle)
                                                                                .category(appointmentWorkItem1Piece2PieceCategory)
                                                                                .build()
                                                                )
                                                                .build()
                                                )
                                        )
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
                        put(Constants.AUTOBOOK_API_ROOT + "/appointments/" + xid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoForUpdate))
                )
                .andExpect(status().isNoContent());

        // verify the *targeted i.e. not the whole* state of the database via the entity model

        appointmentEntityList = fetchWholeAppointmentEntityModel();

        assertThat(appointmentEntityList).hasSize(1);
        appointmentEntity = appointmentEntityList.get(0);
        assertThat(appointmentEntity.getTitle()).isEqualTo(appointmentTitle);
        assertThat(appointmentEntity.getNote()).isEqualTo(appointmentNote);
        assertThat(appointmentEntity.getDate()).isEqualTo(appointmentDateU); // Updated
        assertThat(appointmentEntity.getTime()).isEqualTo(appointmentTime);
        assertThat(appointmentEntity.getAppointmentNotes()).extracting("note").containsExactlyInAnyOrderElementsOf(List.of(appointmentNote1, appointmentNote2));
        assertThat(appointmentEntity.getAppointmentWorkItems()).extracting("title").containsExactlyInAnyOrderElementsOf(List.of(appointmentWorkItem1Title));

        appointmentWorkItemLaborEntities = appointmentEntity.getAppointmentWorkItems().stream().flatMap(awie -> awie.getAppointmentWorkItemsLabor().stream()).toList();

        assertThat(appointmentWorkItemLaborEntities)
                .hasSize(3)
                .allMatch(awile ->
                        appointmentWorkItem1Labor1Title.equals(awile.getTitle()) &&
                                appointmentWorkItem1Labor1HoursWorked.compareTo(awile.getHoursWorked()) == 0 &&
                                appointmentWorkItem1Labor1Cost.compareTo(awile.getCost()) == 0
                                ||
                                appointmentWorkItem1Labor2TitleU.equals(awile.getTitle()) &&
                                        appointmentWorkItem1Labor2HoursWorkedU.compareTo(awile.getHoursWorked()) == 0 &&
                                        appointmentWorkItem1Labor2CostU.compareTo(awile.getCost()) == 0
                        ||
                                appointmentWorkItem1Labor3TitleU.equals(awile.getTitle()) &&
                                        appointmentWorkItem1Labor3HoursWorkedU.compareTo(awile.getHoursWorked()) == 0 &&
                                        appointmentWorkItem1Labor3CostU.compareTo(awile.getCost()) == 0
                );

        appointmentWorkItemPieceEntities = appointmentEntity.getAppointmentWorkItems().stream().flatMap(awie -> awie.getAppointmentWorkItemsPiece().stream()).toList();

        assertThat(appointmentWorkItemPieceEntities)
                .hasSize(2)
                .allMatch(awipe ->
                        appointmentWorkItem1Piece1Title.equals(awipe.getTitle()) &&
                                Objects.isNull(awipe.getPiece())
                                ||
                                appointmentWorkItem1Piece2Title.equals(awipe.getTitle()) &&
                                        appointmentWorkItem1Piece2PieceId.equals(awipe.getPiece().getId())
                );

        /**
         * API call to GET the whole appointment resource
         */

        mockMvc.perform(
                        get(Constants.AUTOBOOK_API_ROOT + "/appointments/" + xid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-MockUser", "me@whoami.com")
                                .content(objectMapper.writeValueAsString(dtoForUpdate))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                      "title": "Warranty service",
                      "note": "once a year",
                      "date": "2025-12-18",
                      "time": "09:00:00",
                      "appointmentWorkItems": [
                        {
                          "title": "Oil change",
                          "appointmentWorkItemsLabor": [
                            {
                              "title": "Empty old oil",
                              "hoursWorked": 0.50,
                              "cost": 25.50
                            },
                            {
                              "title": "Oil refill",
                              "hoursWorked": 0.79,
                              "cost": 30.11
                            },
                            {
                              "title": "Cleanup",
                              "hoursWorked": 0.48,
                              "cost": 30.11
                            }
                          ],
                          "appointmentWorkItemsPiece": [
                            {
                              "title": "Oil pints",
                              "category": "buy",
                              "piece": null,
                              "cost": 11.11
                            },
                            {
                              "title": "Stronger Oil filter ",
                              "category": "buy",
                              "piece": {
                                "id": 1,
                                "title": "Winter tires",
                                "category": "TIRE"
                              },
                              "cost": 110.11
                            }
                          ]
                        }
                      ],
                      "appointmentNotes": [
                        {
                          "note": "Check the tires soon"
                        },
                        {
                          "note": "Free tire storage for the winter"
                        }
                      ]
                    }
                    """));
    }

    /**
     * From this test, it is required to specify LEFT JOIN FETCH statements for every lazy-loaded association.
     * Without that, the database data required for comparison would be incomplete.
     * @return the whole appointment database model.
     */
    private List<AppointmentEntity> fetchWholeAppointmentEntityModel() {
        return entityManager.createQuery("""
        SELECT ae FROM AppointmentEntity ae
        LEFT JOIN FETCH ae.appointmentNotes an
        LEFT JOIN FETCH ae.appointmentWorkItems awi
        LEFT JOIN FETCH awi.appointmentWorkItemsLabor awil
        LEFT JOIN FETCH awi.appointmentWorkItemsPiece awip
        """, AppointmentEntity.class).getResultList();
    }


}
