package org.appjam.bongbaek.domain.event.dto.request;

import org.appjam.bongbaek.domain.event.dto.common.*;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.domain.event.entity.Relationship;

public record EventUpdateRequestDto (
        HostInfo hostInfo,
        EventInfo eventInfo,
        LocationInfo locationInfo
){
    public Category getCategoryEnum() {
        return Category.of(eventInfo.eventCategory());
    }

    public Relationship getRelationshipEnum() {
        return Relationship.of(eventInfo.relationship());
    }
}
