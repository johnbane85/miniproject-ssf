package myapp.core.miniprojectssf.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myapp.core.miniprojectssf.models.Book;
import myapp.core.miniprojectssf.repositories.BookRepository;

@Service
public class SavedBooksService {

  @Autowired
  private BookRepository bookRepo;

  public void save(List<Book> toSave) {
    bookRepo.save(toSave);
  }

  public Optional<Book> get(String primary_isbn13) {

    String result = bookRepo.get(primary_isbn13).toString();
    if (null == result)
      return Optional.empty();

    return bookRepo.get(primary_isbn13);
  }

  public void savelist(String username, List<Book> toSave) {
    bookRepo.savelist(username, toSave);
  }

  public Optional<String> getlist(String username) {

    String result = bookRepo.getlist(username).toString();
    if (null == result)
      return Optional.empty();

    return bookRepo.getlist(username);
  }

  public void delete(String username, String primary_isbn13) {
    bookRepo.delete(username, primary_isbn13);
  }

}