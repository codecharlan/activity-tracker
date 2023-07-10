package com.charlancodes.acttrack.repository;

import com.charlancodes.acttrack.entities.Task;
import com.charlancodes.acttrack.entities.TaskStatus;
import com.charlancodes.acttrack.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTaskByUser(User user);
    Optional<Task> findTasksByUserAndId(User user, Long id);

    List<Task> findTasksByUserAndTaskStatus(User user, TaskStatus taskStatus);
}
