package com.example.alarm_correlation.Service.Implement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.example.alarm_correlation.DTO.AlarmDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.alarm_correlation.Entity.Alarm;
import com.example.alarm_correlation.Entity.AlarmTreeNode;
import com.example.alarm_correlation.Repository.AlarmRepository;
import com.example.alarm_correlation.Repository.AlarmTreeNodeRepository;
import com.example.alarm_correlation.Service.AlarmService;
import com.example.alarm_correlation.Service.AlarmTreeNodeService;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final AlarmTreeNodeService alarmTreeNodeService;
    private final AlarmTreeNodeRepository alarmTreeNodeRepository;

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
    public ResponseEntity<?> addAllParentsAndChildren(Long id) {
        List<Long> result = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();

        queue.add(id);
        visited.add(id);

        while (!queue.isEmpty()) {
            Long current = queue.poll();
            if (!Objects.equals(id, current)) {
                result.add(current);
            }
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
        
        queue.add(id);
        
        while (!queue.isEmpty()) {
            Long current = queue.poll();
            if (!Objects.equals(id, current)) {
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
        for(Long idResult : result) {
            listResult.add(alarmTreeNodeRepository.findById(id).get());
            Optional<AlarmTreeNode> a = alarmTreeNodeRepository.findById(idResult);
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
        }

        // I want to return result and listResult at the same time
        return ResponseEntity.ok(result);

    }

    @Override
    public ResponseEntity<?> updateAlarm(Alarm alarm) {
        return ResponseEntity.ok(alarmRepository.save(alarm));
    }

    @Override
    public ResponseEntity<?> updateAlarmState(Long id, AlarmDTO alarmDTO) {
        Alarm alarm = alarmRepository.findById(id).get();
        alarm.setState(alarmDTO.getState());
        alarm.setUpdateTime(alarmDTO.getUpdateTime());
        alarmRepository.save(alarm);
        List<AlarmTreeNode> listResult = new ArrayList<>();

        if(alarmDTO.getState().equals("DONE")) {
            List<Long> result = new ArrayList<>();
            Queue<Long> queue = new LinkedList<>();
            Set<Long> visited = new HashSet<>();


            // Get the alarm_tree_node ID of the alarm
            Optional<Alarm> alarm_optional = alarmRepository.findById(id);
            Long nodeId = alarm_optional.get().getAlarmTreeNode().getId();

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

                for(Long idResult : result) {
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
                                    .createTime(alarm.getCreateTime()) // Keep the original createTime
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
                                    .createTime(alarm.getCreateTime()) // Set the createTime to current time
                                    .updateTime(LocalDateTime.now())
                                    .alarmTreeNode(a.get())
                                    .build();
                            alarmRepository.save(alarmResult);
                        }
                    }     
                }
            }
        } else if(alarmDTO.getState().equals("CHANGE")) {
            // if state change to CHANGE
            // set state to change and set update time
            alarm.setState("CHANGE");
            alarm.setUpdateTime(LocalDateTime.now());
            alarmRepository.save(alarm);
            return ResponseEntity.ok(alarm);
        }

        return ResponseEntity.ok(listResult);
        
    }

    @Override
    public ResponseEntity<?> deleteAlarm(Long id) {
        // if alarm has state DONE, it can be deleted
        Optional<Alarm> alarm = alarmRepository.findById(id);
        alarmRepository.deleteById(id);
        return ResponseEntity.ok(alarm);
    }
}
