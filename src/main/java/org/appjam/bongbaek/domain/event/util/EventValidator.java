package org.appjam.bongbaek.domain.event.util;

import org.appjam.bongbaek.domain.event.exception.UnauthorizationException;

import java.util.UUID;

public class EventValidator {

    public static void EventAuthorization(UUID memberUUID, UUID eventAuthorUUID) {
        if(memberUUID != null && memberUUID.equals(eventAuthorUUID)) {
            throw new UnauthorizationException();
        }
    }
}
