package com.charlancodes.acttrack.controller;

import com.charlancodes.acttrack.dtos.requestDto.TaskDto;
import com.charlancodes.acttrack.dtos.requestDto.UserDto;
import com.charlancodes.acttrack.dtos.requestDto.UserLoginDto;
import com.charlancodes.acttrack.entities.Task;
import com.charlancodes.acttrack.entities.TaskStatus;
import com.charlancodes.acttrack.entities.User;
import com.charlancodes.acttrack.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("api/acttrack")
public class UserController {
    private final UserService userService;
    private final HttpSession httpSession;

    public UserController(UserService userService, HttpSession httpSession) {
        this.userService = userService;
        this.httpSession = httpSession;
    }

    @PostMapping("/signup")
    public String createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return "Registration Successful";
    }

    @PostMapping("/login")
    public User logIn(@Valid @RequestBody UserLoginDto userLoginDto) {
        User user = userService.login(userLoginDto.getEmail(), userLoginDto.getPassword());
        httpSession.setAttribute("user_id", user.getId()); //....2
        return user;
    }

    @GetMapping("/signout")
    public String signOut() {
        httpSession.invalidate();
        return "Successfully Logged Out";
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        return userService.fetchUser(Long.parseLong(id));
    }

    @PostMapping("/addTask")
    public String addTask(@Valid @RequestBody TaskDto taskDto) {
        userService.addTask((Long) httpSession.getAttribute("user_id"), taskDto);
        return "Task Successfully Added";
    }

    @GetMapping("/viewTask/{id}")
    public TaskDto fetchTask(@PathVariable String id) {
       Task task = userService.fetchTask((Long) httpSession.getAttribute("user_id"), Long.parseLong(id));
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getTaskStatus());
        return taskDto;
    }

    @GetMapping("/viewTasks")
    public List<Task> fetchAllTasks() {
        return userService.fetchAllTasks((Long) httpSession.getAttribute("user_id"));
    }

    @GetMapping("/pendingTasks")
    public List<Task> fetchPendingTasks() {
        return userService.fetchPendingTasks((Long) httpSession.getAttribute("user_id"));
    }

    @GetMapping("/inProgressTasks")
    public List<Task> fetchInProgressTasks() {
        return userService.fetchInProgressTasks((Long) httpSession.getAttribute("user_id"));
    }

    @GetMapping("/completedTasks")
    public List<Task> fetchCompletedTasks() {
        return userService.fetchCompletedTasks((Long) httpSession.getAttribute("user_id"));
    }

    @PostMapping("/updateTaskStatus/{taskId}")
    public Task updateTaskStatus(@PathVariable String taskId, @RequestParam("status") String status) {
        return userService.updateTaskStatus(Long.parseLong(taskId), (Long) httpSession.getAttribute("user_id"), TaskStatus.valueOf(status));
    }

    @PostMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable String id) {
        userService.deleteTask(Long.parseLong(id), (Long) httpSession.getAttribute("user_id"));
        return "Task Deleted Successfully";
    }

    @PostMapping("/editTask/{id}")
    public Task editTask(@PathVariable String id, @RequestBody TaskDto taskDto) {
        return userService.editTask((Long) httpSession.getAttribute("user_id"), Long.parseLong(id), taskDto);
    }
}
