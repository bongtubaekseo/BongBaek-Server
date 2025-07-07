package org.appjam.bongbaek.domain.event.dto.request;

import java.util.List;

public record EventDeleteRequestDto(
        List<String> eventIds
) {
}
