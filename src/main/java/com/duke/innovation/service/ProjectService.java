package com.duke.innovation.service;

import com.duke.innovation.model.PhDTeam;
import com.duke.innovation.model.Project;
import com.duke.innovation.repository.PhDTeamRepository;
import com.duke.innovation.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final PhDTeamRepository phdTeamRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, PhDTeamRepository phdTeamRepository) {
        this.projectRepository = projectRepository;
        this.phdTeamRepository = phdTeamRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> findByTeamId(Long teamId) {
        return projectRepository.findByTeamId(teamId);
    }

    @Transactional
    public Project addProject(Long teamId, Project project) {
        PhDTeam team = phdTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        project.setTeam(team);
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setField1(projectDetails.getField1());
        project.setField2(projectDetails.getField2());
        project.setField3(projectDetails.getField3());

        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}