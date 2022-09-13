package myapp.core.miniprojectssf.controllers;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myapp.core.miniprojectssf.models.Book;
import myapp.core.miniprojectssf.services.SavedBooksService;

@Controller
@RequestMapping(path = { "/booksearch" })
public class BookSearchController {

  @Autowired
  private SavedBooksService savedBooksSvc;

  @GetMapping
  public String getBook(@RequestParam String primary_isbn13, Model model, HttpSession sess) {

    String username = (String) sess.getAttribute("username");

    Optional<Book> opt = savedBooksSvc.get(primary_isbn13);

    if (opt.isEmpty()) {
      return "book_does_not_exist";
    } else {

      Book book = opt.get();

      model.addAttribute("username", username);
      model.addAttribute("book", book);
      return "book_search_result";

    }

  }

}
