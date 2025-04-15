package com.duke.innovation.controller;

import com.duke.innovation.model.PhDTeam;
import com.duke.innovation.model.Project;
import com.duke.innovation.model.User;
import com.duke.innovation.service.PhDTeamService;
import com.duke.innovation.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PhDController {

    private final PhDTeamService phdTeamService;
    private final ProjectService projectService;

    @Autowired
    public PhDController(PhDTeamService phdTeamService, ProjectService projectService) {
        this.phdTeamService = phdTeamService;
        this.projectService = projectService;
    }

    @GetMapping("/phd-students")
    public String phdStudentsPage(HttpSession session, Model model) {
        // Check if user is logged in and is a PhD student
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            if (user.getUserType() != User.UserType.PHD_STUDENT) {
                return "redirect:/portfolio?info=This page is for PhD students";
            }
        } else {
            return "redirect:/login?info=Please login as a PhD student";
        }

        // Add testimonials and other content for PhD students
        model.addAttribute("testimonials", "Sample testimonials"); // Replace with actual data

        return "phd-students";
    }

    @GetMapping("/add-project")
    public String addProjectPage(HttpSession session, Model model) {
        // Check if user is logged in and is a PhD student
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            if (user.getUserType() != User.UserType.PHD_STUDENT) {
                return "redirect:/portfolio?info=Only PhD students can add projects";
            }
        } else {
            return "redirect:/login?info=Please login as a PhD student";
        }

        model.addAttribute("project", new Project());

        return "add-project";
    }

    @PostMapping("/add-project")
    public String addProject(@ModelAttribute Project project, HttpSession session) {
        // Check if user is logged in and is a PhD student
        User user = (User) session.getAttribute("user");
        if (user == null || user.getUserType() != User.UserType.PHD_STUDENT) {
            return "redirect:/login?info=Please login as a PhD student";
        }

        // Find or create team for the user
        PhDTeam team = null;
        // This is a simplified approach - in a real app, you would need logic to find or create the team
        // based on the user's department, research group, etc.

        if (team == null) {
            team = new PhDTeam();
            team.setName(user.getFullName() + "'s Team");
            team.setDepartment("Unknown Department"); // Replace with actual data
            team = phdTeamService.save(team);
        }

        // Add project to the team
        project = projectService.addProject(team.getId(), project);

        return "redirect:/phd-students?success=Project added successfully";
    }

    @GetMapping("/preview/{projectId}")
    public String previewProject(@PathVariable Long projectId, HttpSession session, Model model) {
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login?info=Please login to preview projects";
        }

        // Get project details
        projectService.findById(projectId).ifPresent(project -> {
            model.addAttribute("project", project);
            model.addAttribute("team", project.getTeam());
        });

        return "preview-project";
    }
}