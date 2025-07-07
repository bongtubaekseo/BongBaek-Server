package org.appjam.bongbaek.domain.event.dto;

import java.util.List;

public record EventDeleteRequestDto(
        List<String> eventIds
) {
}
