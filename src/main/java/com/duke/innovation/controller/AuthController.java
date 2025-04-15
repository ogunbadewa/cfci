package com.duke.innovation.controller;

import com.duke.innovation.model.User;
import com.duke.innovation.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String info,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (info != null) {
            model.addAttribute("info", info);
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        if (userService.authenticate(username, password)) {
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                session.setAttribute("user", user);

                // Redirect based on user type
                if (user.getUserType() == User.UserType.PHD_STUDENT) {
                    return "redirect:/phd-students";
                } else {
                    return "redirect:/portfolio";
                }
            }
        }

        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam User.UserType userType,
                           HttpSession session,
                           Model model) {

        try {
            user.setUserType(userType);
            User registeredUser = userService.register(user);
            session.setAttribute("user", registeredUser);

            // Redirect based on user type
            if (userType == User.UserType.PHD_STUDENT) {
                return "redirect:/phd-students?success=Account created successfully";
            } else {
                return "redirect:/portfolio?success=Account created successfully";
            }

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/?info=You have been logged out";
    }
}