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

    @ManyToOne()
    @JoinColumn(name = "parent_alarm")
    private Alarm parentAlarm;

    @ManyToOne()
    @JoinColumn(name = "child_alarm")
    private Alarm childAlarm;
    
}
