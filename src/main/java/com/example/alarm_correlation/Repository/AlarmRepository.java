package com.example.alarm_correlation.Repository;

import com.example.alarm_correlation.Entity.Alarm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    // Update alarm class_id by alarm_tree_node id
    @Query(value = "UPDATE Alarm a SET a.classId = (SELECT atn.id FROM AlarmTreeNode atn WHERE atn.name = a.name) WHERE a.classId IS NULL LIMIT 1", nativeQuery = true)
    int updateClassIdBasedOnName(Alarm alarm);

    // Get all alarms and their information and class id from id of AlarmTreeNode where AlarmTreeNode.name = Alarm.alarmTreeNode.name
    @Query(value = "SELECT a FROM Alarm as a")
    List<Alarm> findAllAlarms();

    // Find alarmTreeNode by name and get class_id
    @Query("SELECT a.id FROM AlarmTreeNode as a WHERE a.name = ?1")
    Long getIdFindByName(String name);
    
}
