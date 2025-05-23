package com.duke.innovation.controller;

import com.duke.innovation.model.PhDTeam;
import com.duke.innovation.model.User;
import com.duke.innovation.service.PhDTeamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class PortfolioController {

    private final PhDTeamService phdTeamService;

    @Autowired
    public PortfolioController(PhDTeamService phdTeamService) {
        this.phdTeamService = phdTeamService;
    }

    @GetMapping("/portfolio")
    public String showPortfolio(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String stage,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String pi,
            HttpSession session,
            Model model) {

        // Add user to model if logged in
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        // Get teams with filters
        List<PhDTeam> teams;

        // Convert empty strings to null
        industry = (industry != null && industry.isEmpty()) ? null : industry;
        stage = (stage != null && stage.isEmpty()) ? null : stage;
        department = (department != null && department.isEmpty()) ? null : department;
        pi = (pi != null && pi.isEmpty()) ? null : pi;

        if (industry != null || stage != null || department != null || pi != null) {
            teams = phdTeamService.findWithFilters(industry, stage, department, pi);
        } else {
            teams = phdTeamService.findAll();
        }

        // Add filter options from database
        Set<String> industries = phdTeamService.getAllIndustries();
        Set<String> stages = phdTeamService.getAllStages();
        Set<String> departments = phdTeamService.getAllDepartments();
        Set<String> pis = phdTeamService.getAllPrincipalInvestigators();

        model.addAttribute("industries", industries);
        model.addAttribute("stages", stages);
        model.addAttribute("departments", departments);
        model.addAttribute("pis", pis);

        model.addAttribute("teams", teams);

        // Add current filters to model
        model.addAttribute("currentIndustry", industry);
        model.addAttribute("currentStage", stage);
        model.addAttribute("currentDepartment", department);
        model.addAttribute("currentPI", pi);

        return "portfolio";
    }

    @GetMapping("/phd-team/{id}")
    public String showTeamDetails(@PathVariable Long id, HttpSession session, Model model) {
        Optional<PhDTeam> teamOpt = phdTeamService.findById(id);

        if (teamOpt.isPresent()) {
            PhDTeam team = teamOpt.get();
            model.addAttribute("team", team);
            model.addAttribute("members", phdTeamService.getTeamMembers(id));

            // Check if user is logged in
            User user = (User) session.getAttribute("user");
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("isFollowing", user.getFollowing().contains(team));
            }

            return "phd-team";
        } else {
            return "redirect:/portfolio?error=Team not found";
        }
    }

    @GetMapping("/follow/{teamId}")
    public String followTeam(@PathVariable Long teamId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login?info=Please login to follow teams";
        }

        try {
            PhDTeam team = phdTeamService.findById(teamId).orElseThrow(() ->
                    new IllegalArgumentException("Team not found"));

            user.getFollowing().add(team);

            return "redirect:/phd-team/" + teamId + "?success=Now following team";
        } catch (IllegalArgumentException e) {
            return "redirect:/portfolio?error=" + e.getMessage();
        }
    }

    @GetMapping("/unfollow/{teamId}")
    public String unfollowTeam(@PathVariable Long teamId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login?info=Please login to unfollow teams";
        }

        try {
            PhDTeam team = phdTeamService.findById(teamId).orElseThrow(() ->
                    new IllegalArgumentException("Team not found"));

            user.getFollowing().remove(team);

            return "redirect:/phd-team/" + teamId + "?success=Unfollowed team";
        } catch (IllegalArgumentException e) {
            return "redirect:/portfolio?error=" + e.getMessage();
        }
    }
}