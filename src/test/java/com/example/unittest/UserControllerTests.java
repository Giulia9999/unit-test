package com.example.unittest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() throws Exception {
        // Arrange
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");

        Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(user);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"email\":\"john@example.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "John", "john@example.com"));
        userList.add(new User(2L, "Jane", "jane@example.com"));

        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("john@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("jane@example.com"));
    }

    @Test
    public void testGetUserById() throws Exception {
        // Arrange
        User user = new User(1L, "John", "john@example.com");

        Mockito.when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Arrange
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setName("Jane");
        updatedUser.setEmail("jane@example.com");

        User existingUser = new User(userId, "John", "john@example.com");

        Mockito.when(userService.updateUser(Mockito.any(Long.class), Mockito.any(User.class))).thenReturn(updatedUser);
        Mockito.when(userService.getUserById(userId)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane\",\"email\":\"jane@example.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Arrange
        Long userId = 1L;

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
