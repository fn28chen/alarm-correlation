package com.example.alarm_correlation.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alarm_correlation.Service.AlarmTreeNodeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarmTree")
public class AlarmTreeNodeController {
    private final AlarmTreeNodeService alarmTreeNodeService;

    // Get all child node's information of current node
    @RequestMapping("/findByParentId/{parentId}")
    public ResponseEntity<?> findByParentId(@PathVariable Long parentId) {
        return ResponseEntity.ok(alarmTreeNodeService.findByParentId(parentId));
    }

//    @RequestMapping("/findAllParentsAndChildrenID/{nodeId}")
//    public ResponseEntity<?> findAllParentsAndChildrenID(@PathVariable Long nodeId) {
//        return ResponseEntity.ok(alarmTreeNodeService.findAllParentsAndChildrenID(nodeId));
//    }

    // Get table size
    @RequestMapping("/getTableSize")
    public ResponseEntity<?> getTableSize() {
        return ResponseEntity.ok(alarmTreeNodeService.getTableSize());
    }
}
