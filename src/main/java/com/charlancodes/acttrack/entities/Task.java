package com.charlancodes.acttrack.entities;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "Task")
@Table(name = "task")
public class Task {
    @SequenceGenerator(
            name = "task_id",
            sequenceName = "task_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_id"
    )
    @Id
    private Long id;
    private String title;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp completedAt;
    @Enumerated(
            EnumType.STRING
    )
    private TaskStatus taskStatus;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    private User user;
}
