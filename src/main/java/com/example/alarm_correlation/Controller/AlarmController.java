package com.example.alarm_correlation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alarm_correlation.DTO.AlarmDTO;
import com.example.alarm_correlation.Entity.Alarm;
import com.example.alarm_correlation.Service.AlarmService;

@RestController
@RequestMapping("/api/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    // Get all alarm information
    @RequestMapping("/findAll")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(alarmService.findAllAlarms());
    }

    // Get alarm information by alarm id
    @RequestMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(alarmService.findAlarmById(id));
    }

    // Create a new alarm
    @RequestMapping("/create")
    public ResponseEntity<?> createAlarm(@RequestBody AlarmDTO alarmDTO) {
        return alarmService.createAlarm(mapper(alarmDTO));
    }

    private Alarm mapper(AlarmDTO alarmDTO) {
        Alarm alarm = new Alarm();
        alarm.setName(alarmDTO.getName());
        alarm.setDescription(alarmDTO.getDescription());
        alarm.setState(alarmDTO.getState());
        alarm.setCreateTime(alarmDTO.getCreateTime());
        alarm.setUpdateTime(alarmDTO.getUpdateTime());

        // From alarm name, search and compare with alarmTreeNode's name, if it is equal, then get class_id and set it to alarm
        return alarm;
    }
}
