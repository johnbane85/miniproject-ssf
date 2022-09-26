package myapp.core.miniprojectssf.controllers;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import myapp.core.miniprojectssf.models.Book;
import myapp.core.miniprojectssf.models.User;
import myapp.core.miniprojectssf.services.BookService;
import myapp.core.miniprojectssf.services.UserService;

@Controller
@RequestMapping(path = { "/createuser" })
public class CreateUserController {

  @Autowired
  @Qualifier("redislab")
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private UserService userSvc;

  @Autowired
  private BookService bookSvc;

  @PostMapping
  public String postUser(@RequestBody MultiValueMap<String, String> form, Model model, HttpSession sess) {

    String username = form.getFirst("username");
    String password = form.getFirst("password");
    String email = form.getFirst("email");

    Optional<User> opt = userSvc.get(username);

    if (!opt.isEmpty()) {

      return "user_already_exist";

    }

    userSvc.save(username, password, email);

    String best_seller_category = form.getFirst("best_seller_category");
    sess.setAttribute("best_seller_category", best_seller_category);

    List<Book> book_list = bookSvc.getBooks(best_seller_category);
    sess.setAttribute("book_list", book_list);

    model.addAttribute("book_list", book_list);
    model.addAttribute("best_seller_category", best_seller_category.replaceAll("[^a-zA-Z0-9]", " ").toUpperCase());

    sess.setAttribute("username", username);
    model.addAttribute("username", username);

    return "best_sellers";
  }

}
