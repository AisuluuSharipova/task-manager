package kg.alatoo.task_management.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void securedEndpointsShouldRedirectToOAuthLogin() throws Exception {
        mockMvc.perform(get("/api/protected"))
                .andExpect(status().isFound()) // 302
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/github"));
    }

    @Test
    void swaggerUiShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void registerShouldBeAccessible() throws Exception {
        String jsonBody = """
                {
                    "username": "testuser",
                    "email": "test@example.com",
                    "password": "Password123"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(jsonBody)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void loginRedirectTest() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost/oauth2/authorization/github"));
    }
}
