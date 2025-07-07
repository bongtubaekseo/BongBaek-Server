package org.appjam.bongbaek.domain.event.dto.request;

import java.util.List;
import java.util.UUID;

public record EventDeleteRequestDto (
        List<UUID> eventIds
) {
}
