package com.duke.innovation.controller;

import com.duke.innovation.model.Resource;
import com.duke.innovation.model.User;
import com.duke.innovation.service.ResourceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ResourcesController {

    private final ResourceService resourceService;

    @Autowired
    public ResourcesController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/resources")
    public String showResources(
            @RequestParam(required = false) String category,
            HttpSession session,
            Model model) {

        // Add user to model if logged in
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        // Get resources by category if specified
        List<Resource> readingMaterials;
        List<Resource> usefulLinks;
        List<Resource> networkContacts;

        if (category != null && !category.isEmpty() && !category.equals("All")) {
            readingMaterials = resourceService.findByTypeAndCategory(Resource.ResourceType.READING_MATERIAL, category);
            usefulLinks = resourceService.findByTypeAndCategory(Resource.ResourceType.USEFUL_LINK, category);
            // For network contacts, you might need additional logic to filter by industry or other criteria
            networkContacts = resourceService.findByCategory(category);
        } else {
            readingMaterials = resourceService.findByType(Resource.ResourceType.READING_MATERIAL);
            usefulLinks = resourceService.findByType(Resource.ResourceType.USEFUL_LINK);
            networkContacts = resourceService.findAll(); // Or use a specific type for network contacts
        }

        model.addAttribute("readingMaterials", readingMaterials);
        model.addAttribute("usefulLinks", usefulLinks);
        model.addAttribute("networkContacts", networkContacts);
        model.addAttribute("currentCategory", category == null ? "All" : category);

        return "resources";
    }

    @GetMapping("/educational-resources")
    public String showEducationalResources(HttpSession session, Model model) {
        // Add user to model if logged in
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        // Get resources for PhD students
        List<Resource> vcGuides = resourceService.findByType(Resource.ResourceType.VC_GUIDE);
        List<Resource> commercializationGuides = resourceService.findByType(Resource.ResourceType.COMMERCIALIZATION_GUIDE);
        List<Resource> testimonials = resourceService.findByType(Resource.ResourceType.TESTIMONIAL);

        model.addAttribute("vcGuides", vcGuides);
        model.addAttribute("commercializationGuides", commercializationGuides);
        model.addAttribute("testimonials", testimonials);

        return "educational-resources";
    }
}