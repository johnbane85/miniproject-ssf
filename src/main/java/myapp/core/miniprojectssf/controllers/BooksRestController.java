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
import myapp.core.miniprojectssf.models.Book;
import myapp.core.miniprojectssf.services.SavedBooksService;

// REST controller for REST Endpoint for /api/books
@RestController
@RequestMapping(path = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BooksRestController {

  @Autowired
  private SavedBooksService savedBooksSvc;

  // GET REST Endpoint for ISBN13
  @GetMapping(path = "{primary_isbn13}")
  public ResponseEntity<String> getBooks(@PathVariable(name = "primary_isbn13") String primary_isbn13) {
    Optional<Book> opt = savedBooksSvc.get(primary_isbn13);
    if (opt.isEmpty()) {
      JsonObject payload = Json.createObjectBuilder()
          .add("error", "Cannot find book %s".formatted(primary_isbn13))
          .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(payload.toString());
    }
    Book book = opt.get();
    return ResponseEntity.ok(book.toJson().toString());
  }

}