package myapp.core.miniprojectssf.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import myapp.core.miniprojectssf.models.User;
import myapp.core.miniprojectssf.services.UserService;

@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

  @Autowired
  private UserService userSvc;

  @GetMapping(path = "{username}")
  public ResponseEntity<String> getUser(@PathVariable(name = "username") String username) {
    Optional<User> opt = userSvc.get(username);
    if (opt.isEmpty()) {
      JsonObject payload = Json.createObjectBuilder()
          .add("error", "Cannot find username %s".formatted(username))
          .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(payload.toString());
    }
    User user = opt.get();
    return ResponseEntity.ok(user.toJson().toString());
  }

}
