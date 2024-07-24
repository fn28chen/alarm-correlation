package com.example.alarm_correlation.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.alarm_correlation.Entity.Alarm;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AlarmService {
    public Long getIdFindByName(String name);
    public ResponseEntity<?> createAlarm(Alarm alarm);
    public ResponseEntity<?> findAlarmById(Long id);
    public List<Alarm> findAllAlarms();
    public ResponseEntity<?> updateAlarm(Alarm alarm);
    public ResponseEntity<?> updateAlarmState(Long id, String state, LocalDateTime updateTime);
    public ResponseEntity<?> deleteAlarm(Long id);

}
