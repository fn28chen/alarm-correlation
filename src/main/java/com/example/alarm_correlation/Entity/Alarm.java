package com.example.alarm_correlation.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alarm")
@Data
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String state;
    private String mode;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private AlarmTree alarmTree;

    @ManyToMany
    @JoinTable(name = "alarm_relationship",
            joinColumns = @JoinColumn(name = "alarm_id"),
            inverseJoinColumns = @JoinColumn(name = "relationship_id"))
    private List<Alarm> alarms = new ArrayList<>();
}



