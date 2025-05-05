package kg.alatoo.task_management.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}