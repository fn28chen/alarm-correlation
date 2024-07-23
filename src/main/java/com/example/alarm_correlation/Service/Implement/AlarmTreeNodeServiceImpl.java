package com.example.alarm_correlation.Service.Implement;

import com.example.alarm_correlation.Entity.AlarmTreeNode;
import com.example.alarm_correlation.Repository.AlarmTreeNodeRepository;
import com.example.alarm_correlation.Service.AlarmTreeNodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}