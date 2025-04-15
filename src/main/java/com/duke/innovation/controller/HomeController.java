package com.duke.innovation.controller;

import com.duke.innovation.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model,
                       @RequestParam(required = false) String error,
                       @RequestParam(required = false) String success,
                       @RequestParam(required = false) String info) {

        // Add any alerts
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }
        if (info != null) {
            model.addAttribute("info", info);
        }

        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        return "index";
    }

    @GetMapping("/who-are-you")
    public String whoAreYou(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Redirect based on user type
            if (user.getUserType() == User.UserType.PHD_STUDENT) {
                return "redirect:/phd-students";
            } else {
                return "redirect:/portfolio";
            }
        }

        // If not logged in, show the selection page
        return "who-are-you";
    }
}