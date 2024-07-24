package com.example.alarm_correlation.Controller;

import java.time.LocalDateTime;
import java.util.*;

import com.example.alarm_correlation.Repository.AlarmRepository;
import com.example.alarm_correlation.Repository.AlarmTreeNodeRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/alarm")


public class AlarmController {

    class ResponseDTO {
        private Long nodeId;
        private List<Long> result;
    
        public ResponseDTO(Long nodeId, List<Long> result) {
            this.nodeId = nodeId;
            this.result = result;
        }
    
        public Long getNodeId() {
            return nodeId;
        }
    
        public List<Long> getResult() {
            return result;
        }
    }

    private final AlarmService alarmService;
    private final AlarmTreeNodeService alarmTreeNodeService;
    private final AlarmTreeNodeRepository alarmTreeNodeRepository;
    private final AlarmRepository alarmRepository;

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

    // BFS function to add all parent and children of a node after create alarm
    @RequestMapping("/addAllParentsAndChildrenID/{nodeId}")
    public ResponseEntity<?> addAllParentsAndChildrenID(@PathVariable Long nodeId) {

        List<Long> result = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();

        queue.add(nodeId);
        visited.add(nodeId);

        while (!queue.isEmpty()) {
            Long current = queue.poll();
            result.add(current);
            List<Long> children = alarmTreeNodeService.getChildId(current);
            for (Long child : children) {
                Optional<AlarmTreeNode> a = alarmTreeNodeRepository.findById(child);
                if (a.isPresent() && !visited.contains(child) 
                && !Objects.equals(a.get().getMode(), "KEEPING") 
                && !Objects.equals(a.get().getMode(), null)) {
                    queue.add(child);
                    visited.add(child);
                }
            }
        }
        
        queue.add(nodeId);
        
        while (!queue.isEmpty()) {
            Long current = queue.poll();
            if (!Objects.equals(nodeId, current)) {
                result.add(current);
            }
        
            List<Long> parents = alarmTreeNodeService.getParentId(current);
            Optional<AlarmTreeNode> currentAlarm = alarmTreeNodeRepository.findById(current);
            if(currentAlarm.get().getName().equals("RRU Disconnected") 
            && !visited.contains(6L) && !visited.contains(7L)){
                if (!visited.contains(6L)) {
                    Optional<AlarmTreeNode> al6 = alarmTreeNodeRepository.findById(6L);
                    if(al6.isPresent() && !Objects.equals(al6.get().getMode(), "KEEPING")) {
                        queue.add(6L);
                        visited.add(6L);
                    }
                }
                if (!visited.contains(7L)) {
                    Optional<AlarmTreeNode> al7 = alarmTreeNodeRepository.findById(7L);
                    if(al7.isPresent() && !Objects.equals(al7.get().getMode(), "KEEPING")) {
                        queue.add(7L);
                        visited.add(7L);
                    }
                }
            }
            for (Long parent : parents) {
                Optional<AlarmTreeNode> a = alarmTreeNodeRepository.findById(parent);
                if (a.isPresent() && !visited.contains(parent) 
                && !Objects.equals(a.get().getMode(), "KEEPING")
                && !Objects.equals(a.get().getMode(), null)) {
                    if(a.get().getName().equals("RRU Disconnected") && !Objects.equals(a.get().getMode(), "KEEPING")) {
                        if (!visited.contains(6L)) {
                            queue.add(6L);
                            visited.add(6L);
                        }
                        if (!visited.contains(7L)) {
                            queue.add(7L);
                            visited.add(7L);
                        }
                    }
                    queue.add(parent);
                    visited.add(parent);
                }
            }
        }

        List<AlarmTreeNode> listResult = new ArrayList<>();
        for(Long id : result) {
            try {
                listResult.add(alarmTreeNodeRepository.findById(id).get());
                Optional<AlarmTreeNode> a = alarmTreeNodeRepository.findById(id);
                if(a.isPresent()) {
                    Alarm alarm = Alarm.builder()
                            .id(id)
                            .name(a.get().getName())
                            .mode(a.get().getMode())
                            .description(a.get().getDescription())
                            .state("INIT")
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .alarmTreeNode(a.get())
                            .build();
                    alarmRepository.save(alarm);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error: " + e);
            }
        }

        return ResponseEntity.ok(listResult);
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
        if(alarmDTO.getState().equals("DONE")) {
            List<Long> result = new ArrayList<>();
            Queue<Long> queue = new LinkedList<>();
            Set<Long> visited = new HashSet<>();

            // Get the alarm_tree_node ID of the alarm
            Optional<Alarm> alarm = alarmRepository.findById(id);
            Long nodeId = alarm.get().getAlarmTreeNode().getId();
            
            queue.add(nodeId);
            visited.add(nodeId);

            while (!queue.isEmpty()) {
                Long current = queue.poll();
                result.add(current);
                List<Long> children = alarmTreeNodeService.getChildId(current);
                for (Long child : children) {
                    Optional<AlarmTreeNode> a = alarmTreeNodeRepository.findById(child);
                    if (a.isPresent() && !visited.contains(child) 
                    && Objects.equals(a.get().getMode(), "KEEPING")
                    && !Objects.equals(a.get().getMode(), null)) {
                        queue.add(child);
                        visited.add(child);
                    }
                }
            }

            queue.add(nodeId);

            while (!queue.isEmpty()) {
                Long current = queue.poll();
                if (!Objects.equals(nodeId, current)) {
                    result.add(current);
                }

                List<Long> parents = alarmTreeNodeService.getParentId(current);
                Optional<AlarmTreeNode> currentAlarm = alarmTreeNodeRepository.findById(current);
                if(currentAlarm.get().getName().equals("RRU Disconnected") 
                && !visited.contains(6L) && !visited.contains(7L)){
                    if (!visited.contains(6L)) {
                        Optional<AlarmTreeNode> al6 = alarmTreeNodeRepository.findById(6L);
                        if(al6.isPresent() && !Objects.equals(al6.get().getMode(), "KEEPING")) {
                            queue.add(6L);
                            visited.add(6L);
                        }
                    }
                    if (!visited.contains(7L)) {
                        Optional<AlarmTreeNode> al7 = alarmTreeNodeRepository.findById(7L);
                        if(al7.isPresent() && !Objects.equals(al7.get().getMode(), "KEEPING")) {
                            queue.add(7L);
                            visited.add(7L);
                        }
                    }
                }
                for (Long parent : parents) {
                    Optional<AlarmTreeNode> a = alarmTreeNodeRepository.findById(parent);
                    if (a.isPresent() && !visited.contains(parent) 
                    && !Objects.equals(a.get().getMode(), "KEEPING")
                    && !Objects.equals(a.get().getMode(), null)) {
                        if(a.get().getName().equals("RRU Disconnected") && !Objects.equals(a.get().getMode(), "KEEPING")) {
                            if (!visited.contains(6L)) {
                                queue.add(6L);
                                visited.add(6L);
                            }
                            if (!visited.contains(7L)) {
                                queue.add(7L);
                                visited.add(7L);
                            }
                        }
                        queue.add(parent);
                        visited.add(parent);
                    }
                }

                List<AlarmTreeNode> listResult = new ArrayList<>();
                for(Long idResult : result) {
                    try {
                        listResult.add(alarmTreeNodeRepository.findById(idResult).get());
                        Optional<AlarmTreeNode> a = alarmTreeNodeRepository.findById(idResult);
                        if(a.isPresent()) {
                            if(a.get().getId() == nodeId) {
                                Alarm alarmResult = Alarm.builder()
                                        .id(id)
                                        .name(a.get().getName())
                                        .mode(a.get().getMode())
                                        .description(a.get().getDescription())
                                        .state("DONE")
                                        .createTime(LocalDateTime.now())
                                        .updateTime(LocalDateTime.now())
                                        .alarmTreeNode(a.get())
                                        .build();
                                alarmRepository.save(alarmResult);
                            } else {
                                Alarm alarmResult = Alarm.builder()
                                        .id(idResult)
                                        .name(a.get().getName())
                                        .mode(a.get().getMode())
                                        .description(a.get().getDescription())
                                        .state("INIT")
                                        .createTime(LocalDateTime.now())
                                        .updateTime(LocalDateTime.now())
                                        .alarmTreeNode(a.get())
                                        .build();
                                alarmRepository.save(alarmResult);
                            }
                        }
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Error: " + e);
                    }
                }
            }
            
            return ResponseEntity.ok(new ResponseDTO(nodeId, result));
        }

        return ResponseEntity.ok(alarmService.updateAlarmState(
                id,
                alarmDTO.getState(),
                alarmDTO.getUpdateTime()
        ));
    }

}
