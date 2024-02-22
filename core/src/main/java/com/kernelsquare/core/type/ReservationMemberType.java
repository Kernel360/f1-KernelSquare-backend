package com.kernelsquare.core.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationMemberType {
    MENTOR("MENTOR"),
    MENTEE("MENTEE"),
    OTHER("OTHER");

    private final String description;
}
