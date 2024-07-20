package com.example.alarm_correlation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alarm_correlation.Service.AlarmTreeNodeService;

@RestController
@RequestMapping("/api/alarmTree")
public class AlarmTreeNodeController {
    private final AlarmTreeNodeService alarmTreeNodeService;

    @Autowired
    public AlarmTreeNodeController(AlarmTreeNodeService alarmTreeNodeService) {
        this.alarmTreeNodeService = alarmTreeNodeService;
    }

    // Get all child node's information of current node
    @RequestMapping("/findByParentId/{parentId}")
    public ResponseEntity<?> findByParentId(@PathVariable int parentId) {
        return ResponseEntity.ok(alarmTreeNodeService.findByParentId(parentId));
    }

    // Get parentID of current node
    @RequestMapping("/getParentId/{nodeId}")
    public ResponseEntity<?> getParentId(@PathVariable int nodeId) {
        return ResponseEntity.ok(alarmTreeNodeService.getParentId(nodeId));
    }

    // Get all child node's ID of current node
    @RequestMapping("/getChildId/{parentId}")
    public ResponseEntity<?> getChildId(@PathVariable int parentId) {
        return ResponseEntity.ok(alarmTreeNodeService.getChildId(parentId));
    }

    @PostMapping("/addAllParentsAndChildrenID/{nodeId}")
    public ResponseEntity<?> findAllParentsAndChildrenID(@PathVariable int nodeId) {
        return ResponseEntity.ok(alarmTreeNodeService.findAllParentsAndChildrenID(nodeId));
    }

    // Get table size
    @RequestMapping("/getTableSize")
    public ResponseEntity<?> getTableSize() {
        return ResponseEntity.ok(alarmTreeNodeService.getTableSize());
    }
}
