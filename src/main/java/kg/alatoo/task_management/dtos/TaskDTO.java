package kg.alatoo.task_management.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;

    private String title;
    private String description;
    private String status;
    private String level;
    private Date creationDate;
    private String endDate;
    private Long assignedUserId;
}
