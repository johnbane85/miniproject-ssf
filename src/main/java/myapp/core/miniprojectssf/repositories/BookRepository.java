package myapp.core.miniprojectssf.repositories;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
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
    for (Book book : book_list)
      map.put(book.getPrimary_isbn13(), book.toJson().toString());
    redisTemplate.opsForValue().multiSet(map);

    for (String primary_isbn13 : map.keySet())
      redisTemplate.expire(primary_isbn13, Duration.ofMinutes(cacheTime));
  }

  public void savelist(String username, List<Book> book_list) {

    Map<String, String> map = new HashMap<>();
    if (redisTemplate.hasKey(username + "_favList")) {
      Optional<String> opt = getlist(username);
      String payload = opt.get();

      JSONObject jObject = new JSONObject(payload);
      Iterator<?> keys = jObject.keys();

      while (keys.hasNext()) {
        String key = (String) keys.next();
        String value = jObject.getString(key);
        map.put(key, value);
      }
    }

    for (Book book : book_list) {
      map.put(book.getPrimary_isbn13(), book.toJson().toString());
    }

    JSONObject json = new JSONObject(map);
    redisTemplate.opsForValue().set(username + "_favList", json.toString(), Duration.ofMinutes(cacheTime));

  }

  public Optional<String> getlist(String username) {

    if (!redisTemplate.hasKey(username + "_favList"))
      return Optional.empty();
    String data = redisTemplate.opsForValue().get(username + "_favList").toString();
    return Optional.of(data);
  }

  public Optional<Book> get(String primary_isbn13) {
    if (!redisTemplate.hasKey(primary_isbn13))
      return Optional.empty();
    String data = redisTemplate.opsForValue().get(primary_isbn13).toString();
    return Optional.of(Book.create(data));
  }

  public void delete(String username, String primary_isbn13) {

    Map<String, String> map = new HashMap<>();
    Optional<String> opt = getlist(username);
    String payload = opt.get();

    JSONObject jObject = new JSONObject(payload);
    Iterator<?> keys = jObject.keys();

    while (keys.hasNext()) {
      String key = (String) keys.next();
      String value = jObject.getString(key);
      map.put(key, value);
    }

    map.remove(primary_isbn13);

    JSONObject json = new JSONObject(map);
    redisTemplate.opsForValue().set(username + "_favList", json.toString(), Duration.ofMinutes(cacheTime));
  }

}