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

    private final AlarmTreeNodeRepository alarmTreeNodeRepository;

    @Autowired
    public AlarmTreeNodeServiceImpl(AlarmTreeNodeRepository alarmTreeNodeRepository) {
        this.alarmTreeNodeRepository = alarmTreeNodeRepository;
    }

    // BFS function to find all parent and children of a node
    public List<Integer> findAllParentsAndChildrenID(int nodeId) {
        List<Integer> result = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.offer(nodeId);
        visited.add(nodeId);
        result.add(nodeId);

        // Instead of it, if want to find child, check the parentId, if it is equal to nodeId, then it is child
        while (!queue.isEmpty()) {
            int current = queue.poll();
            List<Integer> children = getChildId(current);
            for (int child : children) {
                if (!visited.contains(child)) {
                    queue.offer(child);
                    visited.add(child);
                    result.add(child);
                }
            }

            List<Integer> parent = getParentId(current);
            for (int p : parent) {
                if (!visited.contains(p)) {
                    queue.offer(p);
                    visited.add(p);
                    result.add(p);
                }
            }
        }

        return result;
    }

    @Override
    public List<AlarmTreeNode> findByParentId(int parentId) {
        return alarmTreeNodeRepository.findByParentId(parentId);
    }

    @Override
    public List<Integer> getParentId(int nodeId) {
        return alarmTreeNodeRepository.getParentId(nodeId);
    }

    @Override
    public List<Integer> getChildId(int nodeId) {
        return alarmTreeNodeRepository.getChildId(nodeId);
    }

    @Override
    public long getTableSize() {
        return alarmTreeNodeRepository.getTableSize();
    }
}