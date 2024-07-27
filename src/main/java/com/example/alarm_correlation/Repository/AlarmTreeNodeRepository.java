package com.example.alarm_correlation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.alarm_correlation.Entity.AlarmTreeNode;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlarmTreeNodeRepository extends JpaRepository<AlarmTreeNode, Integer> {
    // Get all child node's information of current node
    @Query(value = "SELECT * FROM alarm_tree_node as a WHERE a.parentId = :id", nativeQuery = true)
    List<AlarmTreeNode> findByParentId(@Param("id") Long parentId);

    // Get parent node's ID of current node
    @Query(value = "SELECT * FROM alarm_tree_node p WHERE EXISTS (SELECT * FROM alarm_tree_node as c WHERE c.id = :id AND c.parent_id = p.id)", nativeQuery = true)
    List<Long> getParentId(@Param("id") Long nodeId);

    // Get all child node's ID of current node
    @Query(value = "SELECT * FROM alarm_tree_node as a WHERE a.parent_id = :id", nativeQuery = true)
    List<Long> getChildId(@Param("id") Long parentId);

    Optional<AlarmTreeNode> findById(Long id);
    // Find the AlarmTreeNode based on the Alarm's name
    @Query(value = "SELECT * FROM alarm_tree_node as a WHERE a.name = ?1 LIMIT 1", nativeQuery = true)
    AlarmTreeNode findByName(@Param("name") String name);

    @Query("SELECT COUNT(*) FROM AlarmTreeNode")
    long getTableSize();
}
