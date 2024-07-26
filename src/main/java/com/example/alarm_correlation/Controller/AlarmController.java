package com.example.alarm_correlation.Controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
public class AlarmController {

    private final AlarmService alarmService;
    private final AlarmTreeNodeService alarmTreeNodeService;

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
        try {
            Alarm alarm = mapAlarm(alarmDTO);
            return ResponseEntity.ok(alarmService.createAlarm(alarm));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    // BFS function to add all parent and children of a node after create alarm
    @RequestMapping("/addAllParentsAndChildren/{nodeId}")
    public ResponseEntity<?> addAllParentsAndChildren(@PathVariable Long nodeId) {
        return ResponseEntity.ok(alarmService.addAllParentsAndChildren(nodeId));
    }

    // Get all child node's information of current node
    @RequestMapping("/getParentId/{nodeId}")
    public ResponseEntity<?> getParentId(@PathVariable Long nodeId) {
        return ResponseEntity.ok(alarmTreeNodeService.getParentId(nodeId));
    }

    // Get all child node's ID of current node
    @RequestMapping("/getChildId/{parentId}")
    public ResponseEntity<?> getChildId(@PathVariable Long parentId) {
        return ResponseEntity.ok(alarmTreeNodeService.getChildId(parentId));
    }

    // Update Alarm State
    @RequestMapping("/updateAlarmState/{id}")
    public ResponseEntity<?> updateAlarmState(@PathVariable Long id, @RequestBody AlarmDTO alarmDTO) {
        return ResponseEntity.ok(alarmService.updateAlarmState(id, alarmDTO));
    }

    // Delete Alarm
    @DeleteMapping("/deleteAlarm/{id}")
    public ResponseEntity<?> deleteAlarm(@PathVariable Long id) {
        return ResponseEntity.ok(alarmService.deleteAlarm(id));
    }

    // mapper AlarmDTO
    private Alarm mapAlarm(AlarmDTO alarmDTO) {
        Alarm alarm = new Alarm();

        AlarmTreeNode alarmTreeNode = alarmTreeNodeService.findByName(alarmDTO.getName());

        alarm.setName(alarmDTO.getName());
        alarm.setState(alarmDTO.getState());
        alarm.setMode(alarmDTO.getMode().toString());
        alarm.setDescription(alarmDTO.getDescription());
        alarm.setCreateTime(alarmDTO.getCreateTime());
        alarm.setUpdateTime(alarmDTO.getUpdateTime());
        alarm.setAlarmTreeNode(alarmTreeNode);

        return alarm;
    }

}
