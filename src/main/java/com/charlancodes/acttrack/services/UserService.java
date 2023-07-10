package com.charlancodes.acttrack.services;

import com.charlancodes.acttrack.dtos.requestDto.TaskDto;
import com.charlancodes.acttrack.dtos.requestDto.UserDto;
import com.charlancodes.acttrack.entities.Task;
import com.charlancodes.acttrack.entities.TaskStatus;
import com.charlancodes.acttrack.entities.User;

import java.util.List;

public interface UserService {
    void createUser(UserDto userDto);
    User fetchUser(Long id);
    User login(String email, String password);
    void addTask (Long user_id, TaskDto taskDto);
    List<Task> fetchCompletedTasks(Long userId);
    List<Task> fetchInProgressTasks(Long id);
    List<Task> fetchPendingTasks(Long id);
    List<Task> fetchAllTasks(Long id);
    Task fetchTask(Long taskId, Long userId);
    Task updateTaskStatus(Long taskId, Long userId, TaskStatus newStatus);
    void deleteTask(Long taskId, Long userId);
    Task editTask(Long user_id, Long id, TaskDto taskDto);
}
