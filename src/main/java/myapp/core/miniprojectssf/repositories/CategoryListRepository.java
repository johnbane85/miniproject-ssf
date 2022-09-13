package myapp.core.miniprojectssf.repositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryListRepository {

  @Value("${category.cache.duration}")
  private Long cacheTime;

  @Autowired
  @Qualifier("redislab")
  private RedisTemplate<String, Object> redisTemplate;

  public void save(String best_seller_category, String payload) {

    ValueOperations<String, Object> valueOp = redisTemplate.opsForValue();
    valueOp.set(best_seller_category, payload, Duration.ofMinutes(cacheTime));
  }

  public Optional<String> get(String best_seller_category) {

    if (!redisTemplate.hasKey(best_seller_category))
      return Optional.empty();
    String data = redisTemplate.opsForValue().get(best_seller_category).toString();
    return Optional.of(data);
  }

}
