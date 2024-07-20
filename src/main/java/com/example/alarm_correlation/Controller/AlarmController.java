package com.example.alarm_correlation.Controller;

import com.example.alarm_correlation.DTO.AlarmTreeNodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alarm_correlation.DTO.AlarmDTO;
import com.example.alarm_correlation.Entity.Alarm;
import com.example.alarm_correlation.Entity.AlarmTreeNode;
import com.example.alarm_correlation.Service.AlarmService;
import com.example.alarm_correlation.Service.AlarmTreeNodeService;

@RestController
@RequestMapping("/api/alarm")
public class AlarmController {
    private final AlarmService alarmService;
    private final AlarmTreeNodeService alarmTreeNodeService;

    @Autowired
    public AlarmController(AlarmService alarmService, AlarmTreeNodeService alarmTreeNodeService) {
        this.alarmService = alarmService;
        this.alarmTreeNodeService = alarmTreeNodeService;
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
    @PostMapping("/create")
    public ResponseEntity<?> createAlarm(@RequestBody AlarmDTO alarmDTO) {
        return alarmService.createAlarm(mapAlarm(alarmDTO));
    }

    private Alarm mapAlarm(AlarmDTO alarmDTO) {
        Alarm alarm = new Alarm();
        // From alarm name, search and compare with alarmTreeNode's name, if it is equal, then get class_id and set it to alarm
        AlarmTreeNode alarmTreeNode = alarmTreeNodeService.findByName(alarmDTO.getName());
        // Check the AlarmTreeNode's content
        System.out.println(alarmTreeNode);

        alarm.setName(alarmDTO.getName());
        alarm.setDescription(alarmDTO.getDescription());
        alarm.setState(alarmDTO.getState());
        alarm.setMode(alarmDTO.getMode().toString());
        alarm.setCreateTime(alarmDTO.getCreateTime());
        alarm.setUpdateTime(alarmDTO.getUpdateTime());
        alarm.setAlarmTreeNode(alarmTreeNode);

        return alarm;
    }

}
