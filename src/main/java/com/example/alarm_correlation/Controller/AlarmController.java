package com.example.alarm_correlation.Controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

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

    // BFS function to add all parent and children of a node after create alarm
    @RequestMapping("/addAllParentsAndChildrenID/{nodeId}")
    public ResponseEntity<?> addAllParentsAndChildrenID(@PathVariable Long nodeId) {
        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        List<Long> result = new ArrayList<>();
        queue.add(nodeId);
        visited.add(nodeId);
        while (!queue.isEmpty()) {
            Long current = queue.poll();
            result.add(current);
            List<Long> parents = alarmTreeNodeService.getParentId(current);
            for (Long parent : parents) {
                if (!visited.contains(parent)) {
                    queue.add(parent);
                    visited.add(parent);
                }
            }
            
            List<Long> children = alarmTreeNodeService.getChildId(current);
            for (Long child : children) {
                if (!visited.contains(child)) {
                    queue.add(child);
                    visited.add(child);
                }
            }
            
        }
        return ResponseEntity.ok(result);
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
    
    private Alarm mapAlarm(AlarmDTO alarmDTO) {
        Alarm alarm = new Alarm();
        
        AlarmTreeNode alarmTreeNode = alarmTreeNodeService.findByName(alarmDTO.getName());

        alarm.setName(alarmDTO.getName());
        alarm.setState(alarmDTO.getState());
        alarm.setMode(alarmDTO.getMode().toString());
        alarm.setCreateTime(alarmDTO.getCreateTime());
        alarm.setUpdateTime(alarmDTO.getUpdateTime());
        alarm.setAlarmTreeNode(alarmTreeNode);

        return alarm;
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

}
