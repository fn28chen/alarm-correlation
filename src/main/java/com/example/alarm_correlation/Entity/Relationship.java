package com.example.alarm_correlation.Entity;

import jakarta.persistence.*;
import java.lang.Long;

import lombok.Data;

@Entity
@Table(name = "relationship")
@Data
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_alarm")
    private Alarm parentAlarm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_alarm")
    private Alarm childAlarm;
    
}
