package com.team3.devinit_back.hub.service;

import com.team3.devinit_back.hub.dto.HubProfileResponseDto;
import com.team3.devinit_back.hub.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubService {
    private final HubRepository hubRepository;

    public Page<HubProfileResponseDto> getFilteredProfiles(List<String> skillNames,
                                                           String employmentPeriod,
                                                           String sortType,
                                                           Pageable pageable) {
        return hubRepository.findProfilesByFilters(skillNames, employmentPeriod, sortType, pageable);
    }
}