package com.example.alarm_correlation.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AlarmDTO {
    private Long id;
    private String name;
    private String description;
    private String state;
    private String mode;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();
    private Long classId;
    
}
