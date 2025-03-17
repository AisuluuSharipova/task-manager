package kg.alatoo.task_management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
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
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;
}
