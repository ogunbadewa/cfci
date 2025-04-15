package com.duke.innovation.service;

import com.duke.innovation.model.PhDTeam;
import com.duke.innovation.model.TeamMember;
import com.duke.innovation.model.User;
import com.duke.innovation.repository.PhDTeamRepository;
import com.duke.innovation.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Transactional
    public PhDTeam save(PhDTeam team) {
        return phdTeamRepository.save(team);
    }

    @Transactional
    public void delete(Long id) {
        phdTeamRepository.deleteById(id);
    }

    public List<TeamMember> getTeamMembers(Long teamId) {
        return teamMemberRepository.findByTeamId(teamId);
    }

    @Transactional
    public TeamMember addTeamMember(Long teamId, TeamMember member) {
        PhDTeam team = phdTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        member.setTeam(team);
        return teamMemberRepository.save(member);
    }

    @Transactional
    public void removeTeamMember(Long teamId, Long memberId) {
        PhDTeam team = phdTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        TeamMember member = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Team member not found"));

        if (member.getTeam().getId().equals(teamId)) {
            team.removeMember(member);
            teamMemberRepository.delete(member);
        } else {
            throw new IllegalArgumentException("Member does not belong to this team");
        }
    }

    public Set<User> getFollowers(Long teamId) {
        PhDTeam team = phdTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        return team.getFollowers();
    }
}