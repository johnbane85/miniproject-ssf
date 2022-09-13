package myapp.core.miniprojectssf.repositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import myapp.core.miniprojectssf.models.User;

@Repository
public class UserRepository {

  @Value("${user.cache.duration}")
  private Long cacheTime;

  @Autowired
  @Qualifier("redislab")
  private RedisTemplate<String, Object> redisTemplate;

  public void save(String username, String password, String email) {
    ValueOperations<String, Object> valueOp = redisTemplate.opsForValue();
    JsonObject jo = Json.createObjectBuilder()
        .add("username", username)
        .add("password", password)
        .add("email", email)
        .build();
    valueOp.set(username, jo.toString(), Duration.ofMinutes(cacheTime));
  }

  public Optional<User> get(String username) {
    if (!redisTemplate.hasKey(username))
      return Optional.empty();
    String data = redisTemplate.opsForValue().get(username).toString();
    return Optional.of(User.create(data));
  }

}
