package com.charlancodes.acttrack.services;

import com.charlancodes.acttrack.dtos.requestDto.TaskDto;
import com.charlancodes.acttrack.dtos.requestDto.UserDto;
import com.charlancodes.acttrack.entities.Task;
import com.charlancodes.acttrack.entities.TaskStatus;
import com.charlancodes.acttrack.entities.User;
import com.charlancodes.acttrack.exception.CustomAppException;
import com.charlancodes.acttrack.exception.ResourceAlreadyExistException;
import com.charlancodes.acttrack.exception.ResourceNotFoundException;
import com.charlancodes.acttrack.repository.TaskRepository;
import com.charlancodes.acttrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void createUser(UserDto userDto) {
        Optional<User> checkIfUserExistInDb = userRepository.findByEmail(userDto.getEmail());
        if (checkIfUserExistInDb.isPresent()) {
            throw new ResourceAlreadyExistException("User already exists");
        }
        User newUser = new User();
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(userDto.getPassword());
        userRepository.save(newUser);
    }

    @Override
    public User fetchUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
    }

    @Override
    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));
    }

    @Override
    public void addTask(Long userId, TaskDto taskDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        task.setTaskStatus(TaskStatus.PENDING);
        task.setUser(user);
        taskRepository.save(task);
    }

    @Override //''''''''''
    public List<Task> fetchCompletedTasks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        return taskRepository.findTasksByUserAndTaskStatus(user, TaskStatus.COMPLETED);
    }

    @Override
    public List<Task> fetchInProgressTasks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        return taskRepository.findTasksByUserAndTaskStatus(user, TaskStatus.IN_PROGRESS);
    }

    @Override
    public List<Task> fetchPendingTasks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        return taskRepository.findTasksByUserAndTaskStatus(user, TaskStatus.PENDING);
    }

    @Override
    public List<Task> fetchAllTasks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        List<Task> allTaskByUser = taskRepository.findTaskByUser(user);
        if (allTaskByUser.isEmpty()) {
            throw new CustomAppException("No tasks exist");
        }
        return allTaskByUser;
    }
    @Override
    public Task fetchTask(Long userId, Long taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        return taskRepository.findTasksByUserAndId(user, taskId)
                .orElseThrow(() -> new CustomAppException("Task not found"));
    }
    @Override
    public Task updateTaskStatus(Long taskId, Long userId, TaskStatus newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        Task task = taskRepository.findTasksByUserAndId(user, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (newStatus == TaskStatus.IN_PROGRESS) {
            if (task.getTaskStatus() == TaskStatus.PENDING) {
                task.setTaskStatus(TaskStatus.IN_PROGRESS);
                task.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                return taskRepository.save(task);
            } else {
                throw new CustomAppException("Invalid status transition: Task is not in PENDING status");
            }
        } else if (newStatus == TaskStatus.COMPLETED) {
            if (task.getTaskStatus() == TaskStatus.IN_PROGRESS || task.getTaskStatus() == TaskStatus.PENDING) {
                task.setTaskStatus(TaskStatus.COMPLETED);
                task.setCompletedAt(Timestamp.valueOf(LocalDateTime.now()));
                return taskRepository.save(task);
            } else {
                throw new CustomAppException("Invalid status transition: Task is not in IN_PROGRESS or PENDING status");
            }
        } else if (newStatus == TaskStatus.PENDING) {
            if (task.getTaskStatus() == TaskStatus.COMPLETED) {
                task.setTaskStatus(TaskStatus.PENDING);
                task.setCompletedAt(null);
                return taskRepository.save(task);
            } else {
                throw new CustomAppException("Invalid status transition: Task is not in COMPLETED status");
            }
        }

        throw new CustomAppException("Invalid status transition: Unsupported status provided");
    }

    @Override
    public void deleteTask(Long taskId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));

        Optional<Task> obtainTask = taskRepository.findTasksByUserAndId(user, taskId);
        if (obtainTask.isPresent()) {
            taskRepository.deleteById(taskId);
        } else {
            throw new CustomAppException("Task doesn't exist");
        }
    }

    @Override
    public Task editTask(Long userId, Long taskId, TaskDto taskDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));

        Task task = taskRepository.findTasksByUserAndId(user, taskId)
                .orElseThrow(() -> new CustomAppException("Task doesn't exist"));

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(taskDto.getStatus());
        task.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return taskRepository.save(task);
    }
}
