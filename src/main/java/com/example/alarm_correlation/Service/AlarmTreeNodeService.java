package com.example.alarm_correlation.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.alarm_correlation.Entity.AlarmTreeNode;

import java.util.ArrayList;
import java.util.List;

@Service
public interface AlarmTreeNodeService {
    List<AlarmTreeNode> findByParentId(Long parentId);
    List<Long> getParentId(Long nodeId);
    List<Long> getChildId(Long nodeId);
    AlarmTreeNode findByName(String name);
    ArrayList<Long> addAllParentsAndChildrenID(@PathVariable Long nodeId);
    long getTableSize();
}
