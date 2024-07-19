package com.example.alarm_correlation.Service.Implement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.alarm_correlation.Entity.Alarm;
import com.example.alarm_correlation.Repository.AlarmRepository;
import com.example.alarm_correlation.Service.AlarmService;

@Service
public class AlarmServiceImpl implements AlarmService {
    private final AlarmRepository alarmRepository;

    public AlarmServiceImpl(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

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
        alarmRepository.updateClassIdBasedOnName();

        return new ResponseEntity<>("Alarm has been created", HttpStatus.CREATED);
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
