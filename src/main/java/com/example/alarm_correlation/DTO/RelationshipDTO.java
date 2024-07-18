package com.example.alarm_correlation.DTO;

import lombok.Data;

@Data
public class RelationshipDTO {
    private Long id;
    private Long parentAlarmId;
    private Long childAlarmId;
}
