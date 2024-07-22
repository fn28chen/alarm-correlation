package com.example.alarm_correlation.Service.Implement;

import com.example.alarm_correlation.Entity.AlarmTreeNode;
import com.example.alarm_correlation.Repository.AlarmTreeNodeRepository;
import com.example.alarm_correlation.Service.AlarmTreeNodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

@Service
public class AlarmTreeNodeServiceImpl implements AlarmTreeNodeService {

    @Autowired
    AlarmTreeNodeRepository alarmTreeNodeRepository;

    @Override
    public List<AlarmTreeNode> findByParentId(Long parentId) {
        return alarmTreeNodeRepository.findByParentId(parentId);
    }

    // Find and add all parents and children of current node to the list
    @Override
    public List<Long> findAllParentsAndChildrenID(Long nodeId) {
        List<Long> result = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>(); 
        
        queue.add(nodeId);
        visited.add(nodeId);
        while (!queue.isEmpty()) {
            Long current = queue.poll();
            result.add(current);
            List<Long> children = alarmTreeNodeRepository.getChildId(current);
            for (Long child : children) {
                if (!visited.contains(child)) {
                    queue.add(child);
                    visited.add(child);
                }
            }
            List<Long> parents = alarmTreeNodeRepository.getParentId(current);
            for (Long parent : parents) {
                if (!visited.contains(parent)) {
                    queue.add(parent);
                    visited.add(parent);
                }
            }
        }
        return result;
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
}