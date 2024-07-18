package com.example.alarm_correlation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.alarm_correlation.Entity.AlarmTreeNode;

public interface AlarmTreeNodeRepository extends JpaRepository<AlarmTreeNode, Integer> {

}
