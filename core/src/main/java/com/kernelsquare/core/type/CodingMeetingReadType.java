package com.kernelsquare.core.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodingMeetingReadType {
    ALL("all"), OPEN("open"), CLOSED("closed");

    private final String parameter;

    public String getParameter() {
        return parameter;
    }
}
