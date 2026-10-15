package com.my.worldbuilder.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.my.worldbuilder.IntegrationTest;
import com.my.worldbuilder.event.dto.EventRequest;
import com.my.worldbuilder.event.dto.EventResponse;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.world.World;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EventControllerTest extends IntegrationTest {

    private static final String REQUEST_MAPPING = "/worlds";
    private static final UUID MURIM_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID JIN_MU_WON_EVENT_ID = UUID.fromString("aaaaaaaa-1111-1111-1111-111111111111");
    private static final UUID COUNCIL_EVENT_ID = UUID.fromString("bbbbbbbb-2222-2222-2222-222222222222");

    @Nested
    class GetEventsTests {

        @Nested
        class GivenRequestToGetEventsByWorld {

            @Test
            void thenReturnOnlyEventsOwnedByUserWith200StatusCode() throws Exception {
                MvcResult result = getRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/events");
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                List<EventResponse> body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<>() {}
                );
                assertThat(body)
                        .hasSize(2)
                        .extracting(EventResponse::getTitle)
                        .containsExactlyInAnyOrder(
                                "Battle of Murim",
                                "Council of Sects"
                        );
            }
        }
    }

    @Nested
    class GetEventTests {

        @Nested
        class GivenValidEventId {

            @Test
            void thenReturnEventWith200StatusCode() throws Exception {
                MvcResult result = getRequest("/events/" + JIN_MU_WON_EVENT_ID);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                EventResponse body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        EventResponse.class
                );
                assertThat(body.getId()).isEqualTo(JIN_MU_WON_EVENT_ID);
                assertThat(body.getTitle()).isEqualTo("Battle of Murim");
                assertThat(body.getDescription()).isEqualTo("The great battle for the Northern Heavenly Sect");
            }
        }

        @Nested
        class GivenEventDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                MvcResult result = getRequest("/events/" + id);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenEventOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Event World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );

                Event forbiddenEvent = eventRepository.save(
                        Event.builder()
                                .title("Forbidden Event")
                                .description("Should not be accessible")
                                .date(LocalDate.of(2026, 4, 1))
                                .type(EventType.BATTLE)
                                .world(otherWorld)
                                .build()
                );

                MvcResult result =
                        getRequest("/events/" + forbiddenEvent.getId());

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());
            }
        }
    }

    @Nested
    class CreateEventTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenCreateEventAndReturn201StatusCode() throws Exception {
                EventRequest request = new EventRequest(
                        "New Battle",
                        "A new battle begins",
                        LocalDate.of(2026, 5, 10),
                        EventType.BATTLE
                );

                MvcResult result = postRequest(
                        REQUEST_MAPPING + "/" + MURIM_ID + "/events",
                        request
                );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.CREATED.value());

                UUID id = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        UUID.class
                );

                Event event =
                        eventRepository.findById(id).orElseThrow();

                assertThat(event.getTitle())
                        .isEqualTo("New Battle");

                assertThat(event.getDescription())
                        .isEqualTo("A new battle begins");

                assertThat(event.getDate())
                        .isEqualTo(LocalDate.of(2026, 5, 10));

                assertThat(event.getType())
                        .isEqualTo(EventType.BATTLE);

                assertThat(event.getWorld().getId())
                        .isEqualTo(MURIM_ID);
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400WhenFieldsAreBlank() throws Exception {
                long before = eventRepository.count();
                EventRequest request = new EventRequest(
                        "",
                        "",
                        LocalDate.of(2026, 5, 10),
                        EventType.BATTLE
                );
                MvcResult result = postRequest(
                        REQUEST_MAPPING + "/" + MURIM_ID + "/events",
                        request
                );
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(eventRepository.count()).isEqualTo(before);
            }

            @Test
            void thenReturn400WithValidationErrorsAndDoNotCreateEvent() throws Exception {
                long before = eventRepository.count();
                EventRequest request = new EventRequest(
                        "A".repeat(151),
                        "A".repeat(5001),
                        LocalDate.of(2026, 5, 10),
                        EventType.BATTLE
                );
                MvcResult result = postRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/events", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                String response = result.getResponse().getContentAsString();
                assertThat(response)
                        .contains("Title must be less than 150 characters")
                        .contains("Description must be less than 5000 characters");
                assertThat(eventRepository.count()).isEqualTo(before);
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Another User's World")
                                .description("Not owned by testuser")
                                .user(otherUser)
                                .build()
                );

                EventRequest request = new EventRequest(
                        "Unauthorized Battle",
                        "Should not be created",
                        LocalDate.of(2026, 5, 10),
                        EventType.BATTLE
                );

                long before = eventRepository.count();

                MvcResult result = postRequest(
                        REQUEST_MAPPING + "/" + otherWorld.getId() + "/events",
                        request
                );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());

                assertThat(eventRepository.count())
                        .isEqualTo(before);
            }
        }
    }

    @Nested
    class UpdateEventTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenUpdateEventAndReturn204StatusCode() throws Exception {
                EventRequest request = new EventRequest(
                        "Updated Event",
                        "Updated Description",
                        LocalDate.of(2026, 6, 1),
                        EventType.MEETING
                );

                MvcResult result =
                        putRequest("/events/" + COUNCIL_EVENT_ID, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NO_CONTENT.value());

                Event updated =
                        eventRepository.findById(COUNCIL_EVENT_ID).orElseThrow();

                assertThat(updated.getTitle())
                        .isEqualTo("Updated Event");

                assertThat(updated.getDescription())
                        .isEqualTo("Updated Description");

                assertThat(updated.getDate())
                        .isEqualTo(LocalDate.of(2026, 6, 1));

                assertThat(updated.getType())
                        .isEqualTo(EventType.MEETING);
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400AndDoNotModifyEvent() throws Exception {
                Event before = eventRepository.findById(COUNCIL_EVENT_ID).orElseThrow();
                String originalTitle = before.getTitle();
                String originalDescription = before.getDescription();
                EventRequest request = new EventRequest(
                        "",
                        "",
                        LocalDate.of(2026, 6, 1),
                        EventType.BATTLE
                );
                MvcResult result = putRequest("/events/" + COUNCIL_EVENT_ID, request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                Event after = eventRepository.findById(COUNCIL_EVENT_ID).orElseThrow();
                assertThat(after.getTitle()).isEqualTo(originalTitle);
                assertThat(after.getDescription()).isEqualTo(originalDescription);
            }

            @Test
            void thenReturn400WithValidationErrorsAndDoNotModifyEvent()
                    throws Exception {
                Event before = eventRepository.findById(COUNCIL_EVENT_ID)
                        .orElseThrow();

                String originalTitle = before.getTitle();
                String originalDescription = before.getDescription();
                EventRequest request = new EventRequest(
                        "A".repeat(151),
                        "A".repeat(5001),
                        LocalDate.of(2026, 6, 1),
                        EventType.BATTLE
                );
                MvcResult result = putRequest("/events/" + COUNCIL_EVENT_ID, request);
                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.BAD_REQUEST.value());
                String response = result.getResponse().getContentAsString();
                assertThat(response)
                        .contains("Title must be less than 150 characters")
                        .contains("Description must be less than 5000 characters");
                Event after = eventRepository.findById(COUNCIL_EVENT_ID)
                        .orElseThrow();
                assertThat(after.getTitle()).isEqualTo(originalTitle);
                assertThat(after.getDescription()).isEqualTo(originalDescription);
            }
        }

        @Nested
        class GivenEventDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                EventRequest request = new EventRequest(
                        "Updated Event",
                        "Updated Description",
                        LocalDate.of(2026, 6, 1),
                        EventType.BATTLE
                );
                MvcResult result = putRequest("/events/" + id, request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenEventOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Update World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );

                Event forbiddenEvent = eventRepository.save(
                        Event.builder()
                                .title("Original Event")
                                .description("Original Description")
                                .date(LocalDate.of(2026, 4, 1))
                                .type(EventType.BATTLE)
                                .world(otherWorld)
                                .build()
                );

                EventRequest request = new EventRequest(
                        "New Title",
                        "New Description",
                        LocalDate.of(2026, 6, 1),
                        EventType.MEETING
                );

                MvcResult result =
                        putRequest(
                                "/events/" + forbiddenEvent.getId(),
                                request
                        );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());

                Event unchanged =
                        eventRepository.findById(forbiddenEvent.getId())
                                .orElseThrow();

                assertThat(unchanged.getTitle())
                        .isEqualTo("Original Event");

                assertThat(unchanged.getDescription())
                        .isEqualTo("Original Description");
            }
        }
    }

    @Nested
    class DeleteEventTests {

        @Nested
        class GivenValidEventId {

            @Test
            void thenDeleteEventAndReturn204StatusCode() throws Exception {
                Event event =
                        eventRepository.findById(COUNCIL_EVENT_ID).orElseThrow();

                MvcResult result =
                        deleteRequest("/events/" + event.getId());

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NO_CONTENT.value());

                assertThat(eventRepository.findById(event.getId()))
                        .isEmpty();
            }
        }

        @Nested
        class GivenEventDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                MvcResult result = deleteRequest("/events/" + id);
                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenEventOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Delete World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );

                Event forbiddenEvent = eventRepository.save(
                        Event.builder()
                                .title("Forbidden Delete Event")
                                .description("Should not be deleted")
                                .date(LocalDate.of(2026, 4, 1))
                                .type(EventType.BATTLE)
                                .world(otherWorld)
                                .build()
                );

                MvcResult result =
                        deleteRequest("/events/" + forbiddenEvent.getId());

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());

                assertThat(
                        eventRepository.findById(forbiddenEvent.getId())
                ).isPresent();
            }
        }
    }
}