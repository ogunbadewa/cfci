package com.duke.innovation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/phd-students")
    public String phdStudents() {
        return "phd-landing-page"; // This should match the PhD student HTML (resources/templates/phd_students.html)
    }

}