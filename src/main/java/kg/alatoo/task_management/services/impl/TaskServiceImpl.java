package kg.alatoo.task_management.services.impl;

import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.entities.Task;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.repositories.TaskRepository;
import kg.alatoo.task_management.repositories.UserRepository;
import kg.alatoo.task_management.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return convertToDTO(task);
    }

    @Override
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        User assignedUser = userRepository.findById(taskDTO.getAssignedUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task(
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                taskDTO.getStatus(),
                taskDTO.getLevel(),
                new Date(System.currentTimeMillis()),
                Date.valueOf(taskDTO.getEndDate()),
                assignedUser
        );

        return convertToDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        User assignedUser = userRepository.findById(taskDTO.getAssignedUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setLevel(taskDTO.getLevel());
        task.setEndDate(Date.valueOf(taskDTO.getEndDate()));
        task.setAssignedUser(assignedUser);

        return convertToDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setLevel(task.getLevel());
        dto.setCreationDate(task.getCreationDate());
        dto.setEndDate(task.getEndDate().toString());

        if (task.getAssignedUser() != null) {
            dto.setAssignedUserId(task.getAssignedUser().getId());
        } else {
            dto.setAssignedUserId(null);
        }

        return dto;
    }

}
