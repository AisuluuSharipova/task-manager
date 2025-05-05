package kg.alatoo.task_management.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name="tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;
    private String level;
    private LocalDate creationDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;

    public Task(String title, String description, String status, String level, LocalDate creationDate, LocalDate endDate, User assignedUser) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.level = level;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.assignedUser = assignedUser;
    }
}
