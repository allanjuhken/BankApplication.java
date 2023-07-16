package edu.sda26.springcourse.controller;

import edu.sda26.springcourse.model.MyUser;
import edu.sda26.springcourse.service.MyUserService;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MyUserController {

    private final MyUserService myUserService;

    public MyUserController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user")
    public String showRegisterPage(ModelMap modelMap) {
        MyUser myUser = new MyUser();
        modelMap.addAttribute("user", myUser);
        return "register-user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user/register")
    public String registerUser(@ModelAttribute("user") MyUser myUser) {
        myUser.setRole("USER");
        myUserService.save(myUser);
        return "redirect:/";
    }
}
