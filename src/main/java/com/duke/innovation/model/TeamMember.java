package com.duke.innovation.model;

import jakarta.persistence.*;

@Entity
@Table(name = "team_members")
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String role;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "year_in_program")
    private String yearInProgram;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "photo_url")
    private String photoUrl;

    @Lob
    @Column(name = "photo_data")
    private byte[] photoData;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private PhDTeam team;

    // Constructors
    public TeamMember() {
    }

    public TeamMember(String name, String role, PhDTeam team) {
        this.name = name;
        this.role = role;
        this.team = team;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getYearInProgram() {
        return yearInProgram;
    }

    public void setYearInProgram(String yearInProgram) {
        this.yearInProgram = yearInProgram;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public PhDTeam getTeam() {
        return team;
    }

    public void setTeam(PhDTeam team) {
        this.team = team;
    }
}