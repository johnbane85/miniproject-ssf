package myapp.core.miniprojectssf.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import myapp.core.miniprojectssf.models.Book;
import myapp.core.miniprojectssf.services.SavedBooksService;

//controlls saved_books.html
@Controller
@RequestMapping(path = { "/saved_books", "/saved_books.html" })
public class SavedBooksController {

  @Autowired
  private SavedBooksService savedBooksSvc;

  @PostMapping
  public String postSavedBooks(@RequestBody MultiValueMap<String, String> form, Model model, HttpSession sess) {

    String username = (String) sess.getAttribute("username");

    if (isNull(username))
      return "redirect:index.html";

    List<Book> book_list = (List<Book>) sess.getAttribute("book_list");

    List<String> saveIsbn = form.get("primary_isbn13");

    List<Book> toSave = book_list.stream()
        .filter(books -> {
          for (String i : saveIsbn)
            if (i.equals(books.getPrimary_isbn13()))
              return true;
          return false;
        })
        .toList();

    if (toSave.size() > 0) {
      savedBooksSvc.save(toSave);
    }

    String best_seller_category = (String) sess.getAttribute("best_seller_category");
    model.addAttribute("best_seller_category", best_seller_category.replaceAll("[^a-zA-Z0-9]", " ").toUpperCase());

    model.addAttribute("saved_list", toSave);
    model.addAttribute("book_list", book_list);
    model.addAttribute("username", username);

    return "saved_books";
  }

  private boolean isNull(String s) {
    return ((null == s) || (s.trim().length() <= 0));
  }

}
