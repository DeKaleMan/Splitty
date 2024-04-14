package server.service;

import org.springframework.stereotype.Service;
import server.Main;

import java.util.Objects;

@Service
public class AdminService {

    public boolean validatePassword(String password) {
        return Objects.equals(password, Main.password);
    }

}
