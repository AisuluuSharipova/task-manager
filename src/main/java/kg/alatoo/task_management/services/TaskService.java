package kg.alatoo.task_management.services;

import kg.alatoo.task_management.dtos.TaskDTO;

import java.util.List;
import java.util.Map;

public interface TaskService {

    List<TaskDTO> getAllTasks();

    TaskDTO getTaskById(Long id);

    TaskDTO createTask(TaskDTO taskDTO);

    TaskDTO updateTask(Long id, TaskDTO taskDTO);

    TaskDTO partiallyUpdateTask(Long id, Map<String, Object> updates);

    void deleteTask(Long id);
}
