package kg.alatoo.task_management.auth;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
