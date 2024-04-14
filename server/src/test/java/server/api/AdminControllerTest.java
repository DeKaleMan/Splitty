package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import server.service.AdminService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    AdminService adminService;

    AdminController adminController;

    @BeforeEach
    void setup() {
        adminController = new AdminController(adminService);
    }

    @Test
    void validPasswordTest() {
        when(adminService.validatePassword(anyString())).thenReturn(true);
        ResponseEntity<String> response = adminController.validatePassword("valid password");
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("The password is correct", response.getBody());
        verify(adminService, times(1)).validatePassword(anyString());
    }

    @Test
    void invalidPasswordTest() {
        when(adminService.validatePassword(anyString())).thenReturn(false);
        ResponseEntity<String> response = adminController.validatePassword("invalid password");
        assertEquals(HttpStatusCode.valueOf(401), response.getStatusCode());
        assertEquals("The password is incorrect", response.getBody());
        verify(adminService, times(1)).validatePassword(anyString());
    }

}
