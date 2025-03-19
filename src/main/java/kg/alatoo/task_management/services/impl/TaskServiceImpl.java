package kg.alatoo.task_management.services.impl;

import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.entities.Task;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.exceptions.NotFoundException;
import kg.alatoo.task_management.mappers.TaskMapper;
import kg.alatoo.task_management.repositories.TaskRepository;
import kg.alatoo.task_management.repositories.UserRepository;
import kg.alatoo.task_management.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        return taskMapper.toDTO(task);
    }

    @Override
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        User assignedUser = userRepository.findById(taskDTO.getAssignedUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Task task = new Task(
                null,
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                "New",
                taskDTO.getLevel(),
                new Date(System.currentTimeMillis()),
                Date.valueOf(taskDTO.getEndDate()),
                assignedUser
        );

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        User assignedUser = userRepository.findById(taskDTO.getAssignedUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setLevel(taskDTO.getLevel());
        task.setEndDate(Date.valueOf(taskDTO.getEndDate()));
        task.setAssignedUser(assignedUser);

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDTO partiallyUpdateTask(Long id, Map<String, Object> updates) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    task.setTitle((String) value);
                    break;
                case "description":
                    task.setDescription((String) value);
                    break;
                case "status":
                    task.setStatus((String) value);
                    break;
                case "level":
                    task.setLevel((String) value);
                    break;
                case "endDate":
                    if (value instanceof String) {
                        task.setEndDate(Date.valueOf(LocalDate.parse((String) value)));
                    } else if (value instanceof LocalDate) {
                        task.setEndDate(Date.valueOf((LocalDate) value));
                    } else {
                        throw new IllegalArgumentException("Invalid endDate format");
                    }
                    break;
                case "assignedUserId":
                    User assignedUser = userRepository.findById(Long.parseLong(value.toString()))
                            .orElseThrow(() -> new NotFoundException("Assigned user not found"));
                    task.setAssignedUser(assignedUser);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
