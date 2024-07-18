package com.example.alarm_correlation.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "alarm_tree_node")
public class AlarmTreeNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int position;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private AlarmTreeNode parent;
    
    @Column(nullable = false)
    private String type; 

    @Column(nullable = false)
    private String status; 

}
