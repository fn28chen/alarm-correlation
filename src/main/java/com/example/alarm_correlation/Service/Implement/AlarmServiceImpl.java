package com.example.alarm_correlation.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.alarm_correlation.Entity.Alarm;
import com.example.alarm_correlation.Repository.AlarmRepository;
import com.example.alarm_correlation.Service.AlarmService;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    AlarmRepository alarmRepository;

    @Override
    public ResponseEntity<?> findAlarmById(Long id) {
        return ResponseEntity.ok(alarmRepository.findById(id));
    }

    @Override
    public List<Alarm> findAllAlarms() {
        return alarmRepository.findAllAlarms();
    }

    @Override
    public Long getIdFindByName(String name) {
        return alarmRepository.getIdFindByName(name);
    }

    @Override
    public ResponseEntity<?> createAlarm(Alarm alarm) {
        alarmRepository.save(alarm);
        return ResponseEntity.ok(alarm);
    }

    @Override
    public ResponseEntity<?> updateAlarm(Alarm alarm) {
        return ResponseEntity.ok(alarmRepository.save(alarm));
    }

//    @Override
//    public ResponseEntity<?> updateAlarmState(Long id, String state) {
//
//    }

    @Override
    public ResponseEntity<?> deleteAlarm(Long id) {
        alarmRepository.deleteById(id);
        return ResponseEntity.ok("Alarm with id " + id + " has been deleted");
    }
}
