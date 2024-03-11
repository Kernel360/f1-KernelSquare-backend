package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CodingMeetingServiceStatus implements ServiceStatus {
    //error
    CODING_MEETING_NOT_FOUND(5100),
    CODING_MEETING_ALREADY_EXIST(5101),
    FILTER_PARAMETER_NOT_VALID(5102),
    MEMBER_ID_IS_NULL(5103),

    //success
    CODING_MEETING_FOUND(5140),
    CODING_MEETING_ALL_FOUND(5141),
    CODING_MEETING_UPDATED(5142),
    CODING_MEETING_DELETED(5143),
    CODING_MEETING_CREATED(5144),
    CODING_MEETING_CLOSED(5145);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {return code;}
}
