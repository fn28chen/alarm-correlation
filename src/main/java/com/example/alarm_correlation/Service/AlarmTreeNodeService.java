package com.example.alarm_correlation.Service;

import org.springframework.stereotype.Service;

import com.example.alarm_correlation.Entity.AlarmTreeNode;

import java.util.List;

@Service
public interface AlarmTreeNodeService {
    List<AlarmTreeNode> findByParentId(int parentId);
    List<Integer> getParentId(int nodeId);
    List<Integer> getChildId(int nodeId);
    AlarmTreeNode findByName(String name);
    List<Integer> findAllParentsAndChildrenID(int nodeId);
    long getTableSize();
}
