package com.kernelsquare.alertapi.domain.alert.facade;

import com.kernelsquare.domainmongodb.domain.alert.info.AlertInfo;
import com.kernelsquare.alertapi.domain.alert.dto.AlertDto;
import com.kernelsquare.alertapi.domain.alert.mapper.AlertDtoMapper;
import com.kernelsquare.alertapi.domain.alert.service.AlertService;
import com.kernelsquare.alertapi.domain.auth.dto.MemberAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertFacade {
    private final AlertService alertService;
    private final AlertDtoMapper alertDtoMapper;

    public AlertDto.PersonalFindAllResponse findAllAlerts(MemberAdapter memberAdapter) {
        List<AlertInfo> allAlertInfo = alertService.findAllAlerts(alertDtoMapper.toCommand(memberAdapter));

        List<AlertDto.FindAllResponse> responseList = allAlertInfo.stream()
            .map(alertDtoMapper::toFindAllResponse)
            .toList();

        return AlertDto.PersonalFindAllResponse.from(responseList);
    }
}
