package com.example.alarm_correlation.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "relationship")
@Data
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_alarm_id")
    private Long parentAlarmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_alarm_id")
    private Long childAlarmId;
}
