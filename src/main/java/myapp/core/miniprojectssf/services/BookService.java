package myapp.core.miniprojectssf.services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import myapp.core.miniprojectssf.models.Book;
import myapp.core.miniprojectssf.repositories.CategoryListRepository;

@Service
public class BookService {

  @Autowired
  private CategoryListRepository categoryRepo;

  private LocalDate myObj = LocalDate.now();
  private String date = myObj.toString();

  @Value("${API_KEY}")
  private String apiKey;

  @PostConstruct
  public void init() {
    if (Objects.isNull(apiKey))
      System.err.println("API_KEY is not set");
  }

  public List<Book> getBooks(String best_seller_category) {

    // Check if we have the best seller category list cached
    Optional<String> opt = categoryRepo.get(best_seller_category);
    String payload;

    if (opt.isEmpty()) {

      System.out.println("Getting best seller list from nytimes.com");

      String BOOKS_URL = "https://api.nytimes.com/svc/books/v3/lists/" +
          date
          + "/" + best_seller_category + ".json";

      String url = UriComponentsBuilder
          .fromHttpUrl(BOOKS_URL)
          .queryParam("api-key", apiKey)
          .toUriString();

      RequestEntity<Void> req = RequestEntity
          .get(url)
          .build();

      RestTemplate invoke = new RestTemplate();

      ResponseEntity<String> resp = invoke.exchange(req, String.class);

      payload = resp.getBody();

      categoryRepo.save(best_seller_category, payload);

    } else {
      // Retrieve the value for the box
      payload = opt.get();
      // System.out.printf(">>>> cache: %s\n", payload);
    }

    var jsonReader = Json.createReader(new StringReader(payload));
    var messageAsJson = jsonReader.readObject();
    var results = messageAsJson.getJsonObject("results");

    final List<Book> book_list = new LinkedList<>();

    for (JsonValue v : results.getJsonArray("books"))
      book_list.add(Book.create((JsonObject) v));

    return book_list;

  }

}
