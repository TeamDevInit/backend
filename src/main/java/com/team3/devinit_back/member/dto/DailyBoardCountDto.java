package com.team3.devinit_back.member.dto;

import com.team3.devinit_back.member.entity.DailyBoardCount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyBoardCountDto {
    private LocalDate date;
    private int boardCount;

    public static DailyBoardCountDto fromEntity(DailyBoardCount entity) {
        return new DailyBoardCountDto(entity.getDate(), entity.getBoardCount());
    }
}