package kg.alatoo.task_management.mappers;

import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.entities.Task;
import kg.alatoo.task_management.entities.User;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getLevel(),
                task.getCreationDate(),
                task.getEndDate(),
                task.getAssignedUser() != null ? task.getAssignedUser().getId() : null
        );
    }

    public Task toEntity(TaskDTO taskDTO, User assignedUser) {
        if (taskDTO == null) {
            return null;
        }
        return new Task(
                taskDTO.getId(),
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                taskDTO.getStatus(),
                taskDTO.getLevel(),
                taskDTO.getCreationDate(),
                taskDTO.getEndDate(),
                assignedUser
        );
    }
}
