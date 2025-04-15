package com.duke.innovation.repository;

import com.duke.innovation.model.PhDTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PhDTeamRepository extends JpaRepository<PhDTeam, Long> {
    List<PhDTeam> findByDepartment(String department);
    List<PhDTeam> findByIndustry(String industry);
    List<PhDTeam> findByStage(String stage);
    List<PhDTeam> findByPrincipalInvestigator(String pi);

    @Query("SELECT t FROM PhDTeam t WHERE " +
            "(:industry IS NULL OR t.industry = :industry) AND " +
            "(:stage IS NULL OR t.stage = :stage) AND " +
            "(:department IS NULL OR t.department = :department) AND " +
            "(:pi IS NULL OR t.principalInvestigator = :pi)")
    List<PhDTeam> findWithFilters(String industry, String stage, String department, String pi);
}