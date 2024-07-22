package com.example.alarm_correlation.DTO;

import lombok.Data;

@Data
public class AlarmTreeNodeDTO {
    private Long id;
    private String name;
    private String description;
    private String mode;
    private int position;
    private Long parentId;
}
