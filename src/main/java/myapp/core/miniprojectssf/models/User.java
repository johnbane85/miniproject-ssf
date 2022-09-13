package myapp.core.miniprojectssf.models;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class User {

  private String username;
  private String password;
  private String email;

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public static User create(String jsonStr) {
    StringReader reader = new StringReader(jsonStr);
    JsonReader r = Json.createReader(reader);
    return create(r.readObject());
  }

  public static User create(JsonObject jo) {
    User user = new User();
    user.setUsername(jo.getString("username"));
    user.setPassword(jo.getString("password"));
    user.setEmail(jo.getString("email"));
    return user;
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("username", this.username)
        .add("password", this.password)
        .add("email", this.email)
        .build();
  }

}