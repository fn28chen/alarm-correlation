package com.example.alarm_correlation.Repository;

import com.example.alarm_correlation.Entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
}

