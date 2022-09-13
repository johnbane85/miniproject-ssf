package myapp.core.miniprojectssf.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = { "/logout" })
public class LogoutController {

  @GetMapping
  public String getLogout(HttpSession sess) {

    sess.invalidate();

    return "index";
  }

}
