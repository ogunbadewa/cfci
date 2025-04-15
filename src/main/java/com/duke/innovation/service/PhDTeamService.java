package com.duke.innovation.service;

import com.duke.innovation.model.PhDTeam;
import com.duke.innovation.model.TeamMember;
import com.duke.innovation.repository.PhDTeamRepository;
import com.duke.innovation.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhDTeamService {

    private final PhDTeamRepository phdTeamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public PhDTeamService(PhDTeamRepository phdTeamRepository, TeamMemberRepository teamMemberRepository) {
        this.phdTeamRepository = phdTeamRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    public List<PhDTeam> findAll() {
        return phdTeamRepository.findAll();
    }

    public Optional<PhDTeam> findById(Long id) {
        return phdTeamRepository.findById(id);
    }

    public List<PhDTeam> findWithFilters(String industry, String stage, String department, String pi) {
        return phdTeamRepository.findWithFilters(industry, stage, department, pi);
    }

    public Set<String> getAllIndustries() {
        return phdTeamRepository.findAll().stream()
                .map(PhDTeam::getIndustry)
                .filter(industry -> industry != null && !industry.isEmpty())
                .collect(Collectors.toSet());
    }

    public Set<String> getAllStages() {
        return phdTeamRepository.findAll().stream()
                .map(PhDTeam::getStage)
                .filter(stage -> stage != null && !stage.isEmpty())
                .collect(Collectors.toSet());
    }

    public Set<String> getAllDepartments() {
        return phdTeamRepository.findAll().stream()
                .map(PhDTeam::getDepartment)
                .filter(department -> department != null && !department.isEmpty())
                .collect(Collectors.toSet());
    }

    public Set<String> getAllPrincipalInvestigators() {
        return phdTeamRepository.findAll().stream()
                .map(PhDTeam::getPrincipalInvestigator)
                .filter(pi -> pi != null && !pi.isEmpty())
                .collect(Collectors.toSet());
    }

    public List<TeamMember> getTeamMembers(Long teamId) {
        return teamMemberRepository.findByTeamId(teamId);
    }

    // Add the missing save method
    public PhDTeam save(PhDTeam team) {
        return phdTeamRepository.save(team);
    }
}