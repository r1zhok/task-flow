package org.r1zhok.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    String title;

    String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Status status;

    String assigned_to;

    @Column(nullable = false)
    String created_by;

    @Column(nullable = false)
    LocalDateTime created_on;

    LocalDateTime updated_at;
}