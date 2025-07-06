package org.appjam.bongbaek.domain.event.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.domain.event.entity.Relationship;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public class EventCommonDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class HostInfo{

        private String hostName;
        private String hostNickname;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class EventInfo{

        @JsonProperty("event_id")
        private String eventId;

        @JsonProperty("eventCategory")
        private Category eventCategory;

        @JsonProperty("eventRelationship")
        private Relationship eventRelationship;

        @JsonProperty("cost")
        private int cost;

        @JsonProperty("isAttend")
        private Boolean isAttend;

        @JsonProperty("eventDate")
        private LocalDate eventDate;

        @JsonProperty("note")
        private String note;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class LocationInfo{

        private String location;
        private String address;
        private Double latitude;
        private Double longitude;
    }
}
