package com.team3.devinit_back.hub.repository;

import com.team3.devinit_back.hub.dto.HubProfileResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HubCustomRepository {
    Page<HubProfileResponseDto> findProfilesByFilters(List<String> skillNames, String employmentPeriod,
                                                      String sortType, Pageable pageable);
}