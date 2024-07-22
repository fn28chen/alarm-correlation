package com.example.alarm_correlation.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alarm")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String state;
    private String mode;
    private String description;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }

    @JsonIgnore
    @ManyToOne
    private AlarmTreeNode alarmTreeNode;

}



