package myapp.core.miniprojectssf.repositories;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import myapp.core.miniprojectssf.models.Book;

@Repository
public class BookRepository {

  @Value("${book.cache.duration}")
  private Long cacheTime;

  @Autowired
  @Qualifier("redislab")
  private RedisTemplate<String, Object> redisTemplate;

  public void save(Book book) {
    redisTemplate.opsForValue().set(book.getPrimary_isbn13(), book.toJson().toString());
    redisTemplate.expire(book.getPrimary_isbn13(), Duration.ofMinutes(cacheTime));
  }

  public void save(List<Book> book_list) {
    Map<String, String> map = new HashMap<>();
    for (Book b_list : book_list)
      map.put(b_list.getPrimary_isbn13(), b_list.toJson().toString());
    redisTemplate.opsForValue().multiSet(map);

    for (String primary_isbn13 : map.keySet())
      redisTemplate.expire(primary_isbn13, Duration.ofMinutes(cacheTime));
  }

  public Optional<Book> get(String primary_isbn13) {
    if (!redisTemplate.hasKey(primary_isbn13))
      return Optional.empty();
    String data = redisTemplate.opsForValue().get(primary_isbn13).toString();
    return Optional.of(Book.create(data));
  }

}