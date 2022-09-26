package myapp.core.miniprojectssf.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myapp.core.miniprojectssf.models.Book;
import myapp.core.miniprojectssf.services.SavedBooksService;

@Controller
@RequestMapping(path = { "/delete" })
public class DeleteBookController {

  @Autowired
  private SavedBooksService savedBooksSvc;

  @GetMapping
  public String deleteBook(@RequestParam String primary_isbn13, Model model, HttpSession sess) {

    String username = (String) sess.getAttribute("username");

    savedBooksSvc.delete(username, primary_isbn13);

    Optional<String> opt = savedBooksSvc.getlist(username);

    if (opt.isEmpty()) {
      return "favourite_does_not_exist";
    }

    String payload = opt.get();
    JSONObject jObject = new JSONObject(payload);
    Iterator<?> keys = jObject.keys();

    List<Book> fav_list = new LinkedList<>();

    while (keys.hasNext()) {
      String key = (String) keys.next();
      String value = jObject.getString(key);
      fav_list.add(Book.create(value));
    }

    model.addAttribute("username", username);
    model.addAttribute("fav_list", fav_list);

    return "favourite";
  }

}
