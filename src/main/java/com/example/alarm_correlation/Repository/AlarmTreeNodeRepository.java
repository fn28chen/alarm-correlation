package com.example.alarm_correlation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.alarm_correlation.Entity.AlarmTreeNode;

import java.util.List;

public interface AlarmTreeNodeRepository extends JpaRepository<AlarmTreeNode, Integer> {
    // Get all child node's information of current node
    @Query("SELECT a FROM AlarmTreeNode as a WHERE a.parentId = ?1")
    List<AlarmTreeNode> findByParentId(int parentId);

    // Get parentID of current node
    @Query("SELECT a.parentId FROM AlarmTreeNode as a WHERE a.id = ?1")
    List<Integer> getParentId(int nodeId);

    // Get all child node's ID of current node
    @Query("SELECT a.id FROM AlarmTreeNode as a WHERE a.parentId = ?1")
    List<Integer> getChildId(int parentId);

    // Find the AlarmTreeNode based on the Alarm's name
    @Query("SELECT a FROM AlarmTreeNode as a WHERE a.name = ?1")
    AlarmTreeNode findByName(String name);

    @Query("SELECT COUNT(*) FROM AlarmTreeNode")
    long getTableSize();
}
