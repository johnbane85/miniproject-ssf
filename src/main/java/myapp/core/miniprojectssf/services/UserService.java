package myapp.core.miniprojectssf.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myapp.core.miniprojectssf.models.User;
import myapp.core.miniprojectssf.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepo;

  public void save(String username, String password, String email) {
    userRepo.save(username, password, email);
  }

  public Optional<User> get(String username) {
    return userRepo.get(username);
  }

}
