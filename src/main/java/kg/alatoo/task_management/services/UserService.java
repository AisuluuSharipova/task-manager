package kg.alatoo.task_management.services;

import kg.alatoo.task_management.dtos.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getUsersByName(String firstName, String lastName);

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    UserDTO partiallyUpdateUser(Long id, Map<String, Object> updates);

    void deleteUser(Long id);
}
