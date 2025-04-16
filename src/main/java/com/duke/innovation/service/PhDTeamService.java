package com.duke.innovation.service;

import com.duke.innovation.model.PhDTeam;
import com.duke.innovation.model.TeamMember;
import com.duke.innovation.repository.PhDTeamRepository;
import com.duke.innovation.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhDTeamService {

    @Autowired
    private PhDTeamRepository phdTeamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    /**
     * Retrieves all PhD teams from the database
     * @return List of all PhD teams
     */
    public List<PhDTeam> getAllTeams() {
        return phdTeamRepository.findAll();
    }

    /**
     * Alias for getAllTeams to maintain compatibility with PortfolioController
     */
    public List<PhDTeam> findAll() {
        return getAllTeams();
    }

    /**
     * Retrieves a specific PhD team by its ID
     * @param id The ID of the team to retrieve
     * @return Optional containing the PhD team, or empty if not found
     */
    public Optional<PhDTeam> findById(Long id) {
        return phdTeamRepository.findById(id);
    }

    /**
     * Finds teams matching the specified filters
     * @param industry Industry filter
     * @param stage Stage filter
     * @param department Department filter
     * @param pi Principal Investigator filter
     * @return List of matching PhD teams
     */
    public List<PhDTeam> findWithFilters(String industry, String stage, String department, String pi) {
        return phdTeamRepository.findWithFilters(industry, stage, department, pi);
    }

    /**
     * Gets all distinct industries from teams in the database
     * @return Set of distinct industries
     */
    public Set<String> getAllIndustries() {
        return phdTeamRepository.findAll().stream()
                .map(PhDTeam::getIndustry)
                .filter(industry -> industry != null && !industry.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Gets all distinct stages from teams in the database
     * @return Set of distinct stages
     */
    public Set<String> getAllStages() {
        return phdTeamRepository.findAll().stream()
                .map(PhDTeam::getStage)
                .filter(stage -> stage != null && !stage.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Gets all distinct departments from teams in the database
     * @return Set of distinct departments
     */
    public Set<String> getAllDepartments() {
        return phdTeamRepository.findAll().stream()
                .map(PhDTeam::getDepartment)
                .filter(dept -> dept != null && !dept.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Gets all distinct principal investigators from teams in the database
     * @return Set of distinct principal investigators
     */
    public Set<String> getAllPrincipalInvestigators() {
        return phdTeamRepository.findAll().stream()
                .map(PhDTeam::getPrincipalInvestigator)
                .filter(pi -> pi != null && !pi.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Gets team members for a specific team
     * @param teamId Team ID to get members for
     * @return List of team members
     */
    public List<TeamMember> getTeamMembers(Long teamId) {
        return teamMemberRepository.findByTeamId(teamId);
    }

    /**
     * Saves a PhD team to the database
     * @param team The team to save
     * @return The saved team with generated ID
     */
    @Transactional
    public PhDTeam saveTeam(PhDTeam team) {
        if (team.getCreatedAt() == null) {
            team.setCreatedAt(LocalDateTime.now());
        }
        return phdTeamRepository.save(team);
    }

    /**
     * Saves a team member with their photo
     * @param member The team member to save
     * @param photo The photo file uploaded for the member
     * @return The saved team member with ID
     * @throws IOException If there's an error processing the photo
     */
    @Transactional
    public TeamMember saveTeamMember(TeamMember member, MultipartFile photo) throws IOException {
        // First save to get an ID
        TeamMember savedMember = teamMemberRepository.save(member);

        if (photo != null && !photo.isEmpty()) {
            // Store the photo data as a BLOB
            savedMember.setPhotoData(photo.getBytes());

            // Generate a URL to access the photo
            savedMember.setPhotoUrl("/teams/members/" + savedMember.getId() + "/photo");

            // Save again with the photo data and URL
            savedMember = teamMemberRepository.save(savedMember);
        }

        return savedMember;
    }

    /**
     * Deletes a PhD team by its ID
     * @param id The ID of the team to delete
     */
    @Transactional
    public void deleteTeam(Long id) {
        phdTeamRepository.deleteById(id);
    }

    /**
     * Updates an existing team member
     * @param id The ID of the team member to update
     * @param updatedMember The updated team member data
     * @param photo The new photo, if any
     * @return The updated team member
     * @throws IOException If there's an error processing the photo
     */
    @Transactional
    public TeamMember updateTeamMember(Long id, TeamMember updatedMember, MultipartFile photo) throws IOException {
        Optional<TeamMember> existingMemberOpt = teamMemberRepository.findById(id);

        if (existingMemberOpt.isPresent()) {
            TeamMember existingMember = existingMemberOpt.get();

            // Update fields
            existingMember.setName(updatedMember.getName());
            existingMember.setRole(updatedMember.getRole());
            existingMember.setBio(updatedMember.getBio());
            existingMember.setYearInProgram(updatedMember.getYearInProgram());
            existingMember.setLinkedinUrl(updatedMember.getLinkedinUrl());

            // Update photo if provided
            if (photo != null && !photo.isEmpty()) {
                existingMember.setPhotoData(photo.getBytes());
                // URL remains the same since ID hasn't changed
            }

            return teamMemberRepository.save(existingMember);
        }

        return null;
    }
}