package com.duke.innovation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "phd_teams")
public class PhDTeam {

    //basic project details
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String department;

    @Column(name = "founding_year")
    private Integer foundingYear;

    @Column(name = "sold_to")
    private String soldTo;

    @Column
    private String description;

    @Column
    private String industry;

    @Column
    private String stage;

    @Column(name = "principal_investigator")
    private String principalInvestigator;

    @Column(name = "website_url")
    private String websiteUrl;

    // executive summary fields
    @Column(name = "customer_problem")
    private String customerProblem;

    @Column
    private String solution;

    @Column
    private String advantage;

    @Column
    private String opportunity;

    @Column(name = "tech_milestones")
    private String techMilestones;

    @Column(name = "target_milestones")
    private String targetMilestones;

    @Column(name = "areas_of_need")
    private String areasOfNeed;

    
    // end

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamMember> members = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();



    public PhDTeam() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getFoundingYear() {
        return foundingYear;
    }

    public void setFoundingYear(Integer foundingYear) {
        this.foundingYear = foundingYear;
    }

    public String getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(String soldTo) {
        this.soldTo = soldTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerProblem() {
    return customerProblem;
    }

    public void setCustomerProblem(String customerProblem) {
        this.customerProblem = customerProblem;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAreasOfNeed() {
    return areasOfNeed;
    }
    
    public void setAreasOfNeed(String areasOfNeed) {
    this.areasOfNeed = areasOfNeed;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getPrincipalInvestigator() {
        return principalInvestigator;
    }

    public void setPrincipalInvestigator(String principalInvestigator) {
        this.principalInvestigator = principalInvestigator;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    // exec sum get/set


    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    public String getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(String opportunity) {
        this.opportunity = opportunity;
    }

    public String getTechMilestones() {
        return techMilestones;
    }

    public void setTechMilestones(String techMilestones) {
        this.techMilestones = techMilestones;
    }

    public String getTargetMilestones() {
        return targetMilestones;
    }

    public void setTargetMilestones(String targetMilestones) {
        this.targetMilestones = targetMilestones;
    }

    // end

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<TeamMember> getMembers() {
        return members;
    }

    public void setMembers(Set<TeamMember> members) {
        this.members = members;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }


    public void addMember(TeamMember member) {
        members.add(member);
        member.setTeam(this);
    }

    public void removeMember(TeamMember member) {
        members.remove(member);
        member.setTeam(null);
    }


}