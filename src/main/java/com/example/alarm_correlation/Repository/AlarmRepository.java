package com.example.alarm_correlation.Repository;

import com.example.alarm_correlation.Entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    
}
