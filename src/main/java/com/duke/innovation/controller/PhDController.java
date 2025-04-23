package com.duke.innovation.controller;

import com.duke.innovation.model.PhDTeam;
import com.duke.innovation.model.TeamMember;
import com.duke.innovation.service.PhDTeamService;
import com.duke.innovation.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teams")
public class PhDController {

    @Autowired
    private PhDTeamService phdTeamService;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    /**
     * Displays the form to add a new PhD team
     */
    @GetMapping("/add")
    public String showAddTeamForm(Model model) {
        model.addAttribute("phdTeam", new PhDTeam());
        model.addAttribute("teamMember", new TeamMember());
        return "add-phd-team";
    }

    @GetMapping
    public String listAllTeams(Model model) {
        List<PhDTeam> teams = phdTeamService.getAllTeams();
        model.addAttribute("teams", teams);
        return "components/portfolio";  // Adjust based on your actual .html file
    }


    /**
     * Handles the preview request for a new PhD team
     */
    @PostMapping("/preview")
    public String previewTeam(@ModelAttribute PhDTeam phdTeam,
                              @RequestParam(value = "memberName", required = false) List<String> memberNames,
                              @RequestParam(value = "memberRole", required = false) List<String> memberRoles,
                              @RequestParam(value = "memberBio", required = false) List<String> memberBios,
                              @RequestParam(value = "memberYearInProgram", required = false) List<String> memberYears,
                              @RequestParam(value = "memberLinkedinUrl", required = false) List<String> memberLinkedins,
                              @RequestParam(value = "memberPhoto", required = false) List<MultipartFile> memberPhotos,
                              Model model) {

        // Initialize any null lists
        if (memberNames == null) memberNames = new ArrayList<>();
        if (memberRoles == null) memberRoles = new ArrayList<>();
        if (memberBios == null) memberBios = new ArrayList<>();
        if (memberYears == null) memberYears = new ArrayList<>();
        if (memberLinkedins == null) memberLinkedins = new ArrayList<>();
        if (memberPhotos == null) memberPhotos = new ArrayList<>();


        List<TeamMember> members = new ArrayList<>();
        for (int i = 0; i < memberNames.size(); i++) {
            if (!memberNames.get(i).isEmpty()) {
                TeamMember member = new TeamMember();
                member.setName(memberNames.get(i));

                if (i < memberRoles.size()) member.setRole(memberRoles.get(i));
                if (i < memberBios.size()) member.setBio(memberBios.get(i));
                if (i < memberYears.size()) member.setYearInProgram(memberYears.get(i));
                if (i < memberLinkedins.size()) member.setLinkedinUrl(memberLinkedins.get(i));

                // Store a temporary photo URL for preview
                if (i < memberPhotos.size() && !memberPhotos.get(i).isEmpty()) {
                    member.setPhotoUrl("/temp-preview-photo/" + memberPhotos.get(i).getOriginalFilename());
                }

                members.add(member);
            }
        }

        model.addAttribute("phdTeam", phdTeam);
        model.addAttribute("teamMembers", members);
        model.addAttribute("isPreview", true);

        return "add-phd-team";
    }

    /**
     * Handles the final submission to save a new PhD team
     */
    @PostMapping("/save")
    public String saveTeam(@ModelAttribute PhDTeam phdTeam,
                           @RequestParam(value = "memberName", required = false) List<String> memberNames,
                           @RequestParam(value = "memberRole", required = false) List<String> memberRoles,
                           @RequestParam(value = "memberBio", required = false) List<String> memberBios,
                           @RequestParam(value = "memberYearInProgram", required = false) List<String> memberYears,
                           @RequestParam(value = "memberLinkedinUrl", required = false) List<String> memberLinkedins,
                           @RequestParam(value = "memberPhoto", required = false) List<MultipartFile> memberPhotos,
                           RedirectAttributes redirectAttributes) {

        // Initialize any null lists
        if (memberNames == null) memberNames = new ArrayList<>();
        if (memberRoles == null) memberRoles = new ArrayList<>();
        if (memberBios == null) memberBios = new ArrayList<>();
        if (memberYears == null) memberYears = new ArrayList<>();
        if (memberLinkedins == null) memberLinkedins = new ArrayList<>();
        if (memberPhotos == null) memberPhotos = new ArrayList<>();

        // If this is an existing entity but trying to create a new one with the same ID
        if (phdTeam.getId() != null) {
            Optional<PhDTeam> existingTeam = phdTeamService.findById(phdTeam.getId());
            if (!existingTeam.isPresent()) {
                // If no team exists with this ID, set ID to null to allow auto-generation
                phdTeam.setId(null);
            }
        }

        // First save the team to get an ID
        PhDTeam savedTeam = phdTeamService.saveTeam(phdTeam);

        // Then save each team member
        for (int i = 0; i < memberNames.size(); i++) {
            if (!memberNames.get(i).isEmpty()) {
                TeamMember member = new TeamMember();
                member.setName(memberNames.get(i));

                if (i < memberRoles.size()) member.setRole(memberRoles.get(i));
                if (i < memberBios.size()) member.setBio(memberBios.get(i));
                if (i < memberYears.size()) member.setYearInProgram(memberYears.get(i));
                if (i < memberLinkedins.size()) member.setLinkedinUrl(memberLinkedins.get(i));

                member.setTeam(savedTeam);

                try {
                    MultipartFile photo = i < memberPhotos.size() ? memberPhotos.get(i) : null;
                    TeamMember savedMember = phdTeamService.saveTeamMember(member, photo);
                    savedTeam.addMember(savedMember);
                } catch (IOException e) {
                    // Log the error and continue
                    e.printStackTrace();
                }
            }
        }

        // Update the team with the new members
        phdTeamService.saveTeam(savedTeam);

        redirectAttributes.addFlashAttribute("message", "PhD Team successfully created!");
        return "redirect:/teams/add";
    }

    /**
     * Endpoint to serve team member photos from the database
     */
    @GetMapping("/members/{memberId}/photo")
    @ResponseBody
    public ResponseEntity<byte[]> getMemberPhoto(@PathVariable Long memberId) {
        TeamMember member = teamMemberRepository.findById(memberId).orElse(null);

        if (member == null || member.getPhotoData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type if needed

        return new ResponseEntity<>(member.getPhotoData(), headers, HttpStatus.OK);
    }
}