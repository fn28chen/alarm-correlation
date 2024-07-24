package com.example.alarm_correlation.Service.Implement;

import com.example.alarm_correlation.Entity.AlarmTreeNode;
import com.example.alarm_correlation.Repository.AlarmTreeNodeRepository;
import com.example.alarm_correlation.Service.AlarmTreeNodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class AlarmTreeNodeServiceImpl implements AlarmTreeNodeService {

    @Autowired
    AlarmTreeNodeRepository alarmTreeNodeRepository;
    AlarmTreeNodeService alarmTreeNodeService;

    @Override
    public List<AlarmTreeNode> findByParentId(Long parentId) {
        return alarmTreeNodeRepository.findByParentId(parentId);
    }

    // Get parent node's ID of current node
    @Override
    public List<Long> getParentId(Long nodeId) {
        return alarmTreeNodeRepository.getParentId(nodeId);
    }

    // Get all child node's ID of current node
    @Override
    public List<Long> getChildId(Long nodeId) {
        return alarmTreeNodeRepository.getChildId(nodeId);
    }

    // Get table size of alarm_tree_node
    @Override
    public long getTableSize() {
        return alarmTreeNodeRepository.getTableSize();
    }

    // Find the AlarmTreeNode based on the Alarm's name
    @Override
    public AlarmTreeNode findByName(String name) {
        return alarmTreeNodeRepository.findByName(name);
    }

    // Add all parents and children ID of the current node
    @Override
    public ArrayList<Long> addAllParentsAndChildrenID(@PathVariable Long nodeId) {

        ArrayList<Long> result = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();

        queue.add(nodeId);
        visited.add(nodeId);

        while(!queue.isEmpty()) {
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
                    if(a.get().getName().equals("RRU Disconnected")) {
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

        return result;
    }
}