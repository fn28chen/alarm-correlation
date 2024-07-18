package com.example.alarm_correlation.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "alarm_tree")
@Data
public class AlarmTree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int classId;
    @Column(nullable = false)
    private int level;
    @OneToMany(mappedBy = "alarm_tree")
    private List<Alarm> alarms;
}
