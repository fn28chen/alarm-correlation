package com.example.alarm_correlation.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.alarm_correlation.Entity.Alarm;

import java.util.List;

@Service
public interface AlarmService {
    public Long getIdFindByName(String name);
    public ResponseEntity<?> createAlarm(Alarm alarm);
    public ResponseEntity<?> updateAlarm(Alarm alarm);
    public ResponseEntity<?> deleteAlarm(Long id);
    public ResponseEntity<?> findAlarmById(Long id);
    public List<Alarm> findAllAlarms();
//    public ResponseEntity<?> updateAlarmState(Long id, String state);
}
